package com.gy.resource.constant;

/**
 * @author: gaolanyu
 * @date: 2020-02-14
 * @remark:
 * //发布状态，0、待审核，1、系统审核通过，2、待人工审核，3、人工审核通过，4、人工审核不通过
 * //是否置顶，0：未置顶、1：已置顶
 * //是否包含敏感词 0：不包含、1：包含
 * //是否有图 0：否、1：是
 */
public interface ResourceConstant {
    interface platform{
        int weixin=1;
    }
    interface check{
        int check_init=0;
        int system_check_success=1;
        int person_check_need=2;
        int person_check_success=3;
        int person_check_fail=4;
    }
    interface top{
        int down=0;
        int top=1;
    }
    interface sensitive{
        int no_contanins=0;
        int contatanins=1;
    }

    interface imageFlag{
        int no=0;
        int yes=1;
    }

    interface deleteFlag{
        int no=0;
        int yes=1;
    }

    interface brownSort{
        int up=0;
        int down=1;
    }

    interface shareSort{
        int up=0;
        int down=1;
    }

    interface sortType{
        String brownUp="0";
        String shareUp="1";
        String phoneUp="2";
    }
}
