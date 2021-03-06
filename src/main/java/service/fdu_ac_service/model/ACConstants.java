package service.fdu_ac_service.model;

public class ACConstants {
    public ACConstants() {
    }

    public static final int WHITE = 1;
    public static final int BLACK = 0;
    public static final int NORMAL = 2;

    public static final int EXPORTABLE = 1; // 可导出
    public static final int NON_EXPORTABLE = 0; // 不可导出

    public static final int TYPE_DELETEBLACK = 0; // 删除黑名单的申请
    public static final int TYPE_ADDWHITE = 1; // 添加白名单的申请
    public static final int TYPE_VISIT = 2; // 查看数据的申请

    public static final int VALUE_DEFAULT = -1; //查看数据的申请,新建voteAction时,无需暂存user_id,此为默认值

    public static final int STATUS_UNDERWAY = 0; // 申请状态 进行中
    public static final int STATUS_FINISH_SUCCESS = 1; // 申请状态 已结束 成功
    public static final int STATUS_FINISH_FAIL = 2;    // 申请状态 已结束 失败

    public static final int DECISON_DEFAULT = -1; //用户决策 初始化
    public static final int DECISION_DENY = 0; // 用户决策 拒绝申请
    public static final int DECISION_PERMIT = 1; // 用户决策 同意申请
    public static final int DECISION_GIVEUP = 2; // 用户决策 弃权

}