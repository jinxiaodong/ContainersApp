package com.project.container.containersapp.frame.constants;

/**
 * Created by xiaodong.jin on 2018/3/15.
 * description：请求地址
 */

public class HttpContstants {
    /*域名*/
    public static String HTTP_BASE = "http://118.89.230.151/";


    /*登录接口*/
    public static String LOGIN = HTTP_BASE + "jzx/api/login";

    /*验箱列表*/
    public static String CHECK_BOX = HTTP_BASE + "jzx/api/yanxianglist";

    /*箱损类型*/
    //jzx/api/posunleixing
    public static String TYPE_XIANGSUN = HTTP_BASE + "jzx/api/posunleixing";

    /*更新集装箱破损状态*/
    //jzx/api/updatexiangsun?auth=QwFu-MWb3OmGvt2T9ne5-tkuP4_5nk0G&zyjzxdm=TBJU1000005&pszt=AB
    public static String UPDATE_XIANGSUN = HTTP_BASE + "jzx/api/updatexiangsun";

    /*验箱通过*/
    //验箱通过接口地址：jzx/api/yanxiangpass?auth=QwFu-MWb3OmGvt2T9ne5-tkuP4_5nk0G&zydm=2
    public static String CHECKBOX_PASS = HTTP_BASE + "jzx/api/yanxiangpass";

    /*换箱接口*/
    public static String CHECKBOX_EXCHANGE = HTTP_BASE + "jzx/api/huanxiang";

    /*自动获取可用空箱*/
    //jzx/api/jzxautocomplete?auth=xxx&term=xxx
    public static String AUTO_COMPLETE_BOX = HTTP_BASE + "jzx/api/jzxautocomplete";

    /*更新作业状态*/
    //jzx/api/updatezuoye?auth=xxx
    public static String UPDATE_ZY_STATUS = HTTP_BASE + "jzx/api/updatezuoye";


    /*
    吊箱列表接口：/jzx/api/diaoxianglist?auth=xxx
    吊箱过程列表接口：jzx/api/diaoxiangdan?auth=xxx&zydm=xxx
    * */
    /*吊箱列表*/
    public static String DX_LIST = HTTP_BASE + "jzx/api/diaoxianglist";

    /*吊箱过程列表*/
    public static String DX_PROCESS_LIST = HTTP_BASE + "jzx/api/diaoxiangdan";

    /*吊箱更新接口*/
    public static String DX_UPDATE_COMPLETE = HTTP_BASE + "jzx/api/diaoxiangdone";

    /*装箱列表*/
    public static String ZX_LIST = HTTP_BASE + "jzx/api/zhuangxianglist";
    /*装箱完成更新接口*/
    public static String ZX_FINISH = HTTP_BASE + "jzx/api/zhuangxiangdone";

    /*检斤验货*/
    public static String  CHECK_GOODS_LIST = HTTP_BASE+"jzx/api/jjyhlist";
    /*检斤验货提交*/
    public static String  CHECK_GOODS_DONE = HTTP_BASE+"jzx/api/jjyhdone";

}
