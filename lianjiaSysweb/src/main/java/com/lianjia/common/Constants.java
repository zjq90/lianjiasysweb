package com.lianjia.common;

public class Constants {

    public static final String DEFAULT_SUCCESS_MESSAGE = "操作成功";
    public static final String DEFAULT_ERROR_MESSAGE = "操作失败";

    public static final Integer STATUS_SUCCESS = 200;
    public static final Integer STATUS_ERROR = 500;
    public static final Integer STATUS_UNAUTHORIZED = 401;
    public static final Integer STATUS_FORBIDDEN = 403;

    public static final Integer ENABLED = 1;
    public static final Integer DISABLED = 0;
    public static final Integer DELETED = 0;
    public static final Integer NORMAL = 1;

    public static final String YES = "1";
    public static final String NO = "0";

    public static final Integer DEFAULT_PAGE_NUM = 1;
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    public static final String ORDER_TYPE_SERVICE = "service";
    public static final String ORDER_TYPE_PACKAGE = "package";

    public static final String ORDER_STATUS_PENDING_PAY = "pending_pay";
    public static final String ORDER_STATUS_PENDING_CONFIRM = "pending_confirm";
    public static final String ORDER_STATUS_PENDING_SERVICE = "pending_service";
    public static final String ORDER_STATUS_IN_SERVICE = "in_service";
    public static final String ORDER_STATUS_PENDING_SIGN = "pending_sign";
    public static final String ORDER_STATUS_COMPLETED = "completed";
    public static final String ORDER_STATUS_CANCELLED = "cancelled";
    public static final String ORDER_STATUS_REFUNDED = "refunded";

    public static final String TASK_STATUS_PENDING = "pending";
    public static final String TASK_STATUS_ACCEPTED = "accepted";
    public static final String TASK_STATUS_ON_WAY = "on_way";
    public static final String TASK_STATUS_ARRIVED = "arrived";
    public static final String TASK_STATUS_IN_SERVICE = "in_service";
    public static final String TASK_STATUS_COMPLETED = "completed";
    public static final String TASK_STATUS_CANCELLED = "cancelled";

    public static final String CHECKIN_TYPE_ARRIVE = "arrive";
    public static final String CHECKIN_TYPE_START = "start";
    public static final String CHECKIN_TYPE_COMPLETE = "complete";

    public static final String REPORT_TYPE_DETECTION = "detection";
    public static final String REPORT_TYPE_SERVICE = "service";

    public static final String PHOTO_TYPE_BEFORE = "before";
    public static final String PHOTO_TYPE_AFTER = "after";

    public static final String COUPON_TYPE_DISCOUNT = "discount";
    public static final String COUPON_TYPE_CASH = "cash";
    public static final String COUPON_TYPE_FULL_REDUCTION = "full_reduction";

    public static final String MEMBER_TYPE_ADULT = "adult";
    public static final String MEMBER_TYPE_CHILD = "child";
    public static final String MEMBER_TYPE_ELDER = "elder";
    public static final String MEMBER_TYPE_PET = "pet";

    public static final String TAG_CATEGORY_USER = "user";
    public static final String TAG_CATEGORY_FAMILY = "family";
    public static final String TAG_CATEGORY_SERVICE = "service";

    public static final String PAY_TYPE_WECHAT = "wechat";
    public static final String PAY_TYPE_BALANCE = "balance";

    public static final String SOURCE_PURCHASE = "purchase";
    public static final String SOURCE_GIFT = "gift";
    public static final String SOURCE_ACTIVITY = "activity";
    public static final String SOURCE_RECEIVE = "receive";
    public static final String SOURCE_PROMOTION = "promotion";
}
