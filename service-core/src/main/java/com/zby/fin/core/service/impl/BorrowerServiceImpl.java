package com.zby.fin.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zby.fin.core.enums.BorrowerStatusEnum;
import com.zby.fin.core.enums.IntegralEnum;
import com.zby.fin.core.mapper.BorrowerAttachMapper;
import com.zby.fin.core.mapper.BorrowerMapper;
import com.zby.fin.core.mapper.UserInfoMapper;
import com.zby.fin.core.mapper.UserIntegralMapper;
import com.zby.fin.core.pojo.entity.Borrower;
import com.zby.fin.core.pojo.entity.BorrowerAttach;
import com.zby.fin.core.pojo.entity.UserInfo;
import com.zby.fin.core.pojo.entity.UserIntegral;
import com.zby.fin.core.pojo.vo.BorrowerApprovalVO;
import com.zby.fin.core.pojo.vo.BorrowerAttachVO;
import com.zby.fin.core.pojo.vo.BorrowerDetailVO;
import com.zby.fin.core.pojo.vo.BorrowerVO;
import com.zby.fin.core.service.BorrowerAttachService;
import com.zby.fin.core.service.BorrowerService;
import com.zby.fin.core.service.DictService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 借款人 服务实现类
 * </p>
 *
 * @author Zhang Bingyuan
 * @since 2022-03-01
 */
@Service
public class BorrowerServiceImpl extends ServiceImpl<BorrowerMapper, Borrower> implements BorrowerService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private BorrowerAttachMapper borrowerAttachMapper;

    @Resource
    private DictService dictService;

    @Resource
    private BorrowerAttachService borrowerAttachService;

    @Resource
    private UserIntegralMapper userIntegralMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveBorrowerVOByUserId(BorrowerVO borrowerVO, Long userId) {

        UserInfo userInfo = userInfoMapper.selectById(userId);

        //保存借款人信息
        Borrower borrower = new Borrower();
        BeanUtils.copyProperties(borrowerVO, borrower);
        borrower.setUserId(userId);
        borrower.setName(userInfo.getName());
        borrower.setIdCard(userInfo.getIdCard());
        borrower.setMobile(userInfo.getMobile());
        borrower.setStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());//认证中
        baseMapper.insert(borrower);

        //保存附件
        List<BorrowerAttach> borrowerAttachList = borrowerVO.getBorrowerAttachList();
        borrowerAttachList.forEach(borrowerAttach -> {
            borrowerAttach.setBorrowerId(borrower.getId());
            borrowerAttachMapper.insert(borrowerAttach);
        });

        //更新会员状态，更新为认证中
        userInfo.setBorrowAuthStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public Integer getStatusByUserId(Long userId) {

        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper.select("status").eq("user_id", userId);
        List<Object> objects = baseMapper.selectObjs(borrowerQueryWrapper);

        if(objects.size() == 0){
            //借款人尚未提交信息
            return BorrowerStatusEnum.NO_AUTH.getStatus();
        }
        Integer status = (Integer)objects.get(0);
        return status;
    }

    @Override
    public IPage<Borrower> listPage(Page<Borrower> pageParam, String keyword) {

        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();

        if(StringUtils.isEmpty(keyword)){
            return baseMapper.selectPage(pageParam, null);
        }

        borrowerQueryWrapper.like("name", keyword)
                .or().like("id_card", keyword)
                .or().like("mobile", keyword)
                .orderByDesc("id");
        return baseMapper.selectPage(pageParam, borrowerQueryWrapper);
    }

    @Override
    public BorrowerDetailVO getBorrowerDetailVOById(Long id) {

        //获取借款人信息
        Borrower borrower = baseMapper.selectById(id);

        //填充基本借款人信息
        BorrowerDetailVO borrowerDetailVO = new BorrowerDetailVO();
        BeanUtils.copyProperties(borrower, borrowerDetailVO);

        //婚否
        borrowerDetailVO.setMarry(borrower.getMarry()?"是":"否");
        //性别
        borrowerDetailVO.setSex(borrower.getSex()==1?"男":"女");

        //计算下拉列表选中内容
        String education = dictService.getNameByParentDictCodeAndValue("education", borrower.getEducation());
        String industry = dictService.getNameByParentDictCodeAndValue("moneyUse", borrower.getIndustry());
        String income = dictService.getNameByParentDictCodeAndValue("income", borrower.getIncome());
        String returnSource = dictService.getNameByParentDictCodeAndValue("returnSource", borrower.getReturnSource());
        String contactsRelation = dictService.getNameByParentDictCodeAndValue("relation", borrower.getContactsRelation());

        //设置下拉列表选中内容
        borrowerDetailVO.setEducation(education);
        borrowerDetailVO.setIndustry(industry);
        borrowerDetailVO.setIncome(income);
        borrowerDetailVO.setReturnSource(returnSource);
        borrowerDetailVO.setContactsRelation(contactsRelation);

        //审批状态
        String status = BorrowerStatusEnum.getMsgByStatus(borrower.getStatus());
        borrowerDetailVO.setStatus(status);

        //获取附件VO列表
        List<BorrowerAttachVO> borrowerAttachVOList =  borrowerAttachService.selectBorrowerAttachVOList(id);
        borrowerDetailVO.setBorrowerAttachVOList(borrowerAttachVOList);

        return borrowerDetailVO;
    }


    @Override
    public void approval(BorrowerApprovalVO borrowerApprovalVO) {

        //借款人认证状态
        Long borrowerId = borrowerApprovalVO.getBorrowerId();
        Borrower borrower = baseMapper.selectById(borrowerId);
        //设置审核状态
        borrower.setStatus(borrowerApprovalVO.getStatus());
        baseMapper.updateById(borrower);

        //获取user的信息
        Long userId = borrower.getUserId();
        UserInfo userInfo = userInfoMapper.selectById(userId);

        //添加积分
        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setUserId(userId);
        userIntegral.setIntegral(borrowerApprovalVO.getInfoIntegral());
        userIntegral.setContent("借款人基本信息");
        userIntegralMapper.insert(userIntegral);

        //把原来的积分也要取出来
        int curIntegral = userInfo.getIntegral() + borrowerApprovalVO.getInfoIntegral();
        //身份证信息
        if(borrowerApprovalVO.getIsIdCardOk()) {
            curIntegral += IntegralEnum.BORROWER_IDCARD.getIntegral();
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_IDCARD.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_IDCARD.getMsg());
            userIntegralMapper.insert(userIntegral);
        }
        //房产信息
        if(borrowerApprovalVO.getIsHouseOk()) {
            curIntegral += IntegralEnum.BORROWER_HOUSE.getIntegral();
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_HOUSE.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_HOUSE.getMsg());
            userIntegralMapper.insert(userIntegral);
        }
        //车信息
        if(borrowerApprovalVO.getIsCarOk()) {
            curIntegral += IntegralEnum.BORROWER_CAR.getIntegral();
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_CAR.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_CAR.getMsg());
            userIntegralMapper.insert(userIntegral);
        }


        userInfo.setIntegral(curIntegral);
        //修改用户信息中的审核状态
        userInfo.setBorrowAuthStatus(borrowerApprovalVO.getStatus());
        userInfoMapper.updateById(userInfo);

    }

}
