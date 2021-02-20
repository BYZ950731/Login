package com.tonmx.inspection;

public interface TMConstantData {

    interface FormName {
        interface DBCollums {
            String ID = "id";
            String USERID = "userid";
            String SHEETID = "sheetid";
            String SHEETNAME = "sheetname";
            String FILENAME = "filename";
            String UPDATETIME = "updatetime";
            String CREATETIME = "creattime";
            String SHEETLOGID = "sheetlogid";
            String REVIEWER = "reviewer";
            String REVIEWERID = "reviewerid";
            String INSPECTOR = "inspector";
            String STATE = "state";


        }
    }

    interface Template {
        interface DBCollums {
            String ID = "id";
            String FORMID = "formid";
            String SHEETID = "sheetid";
            String NUMBER = "number";
            String PAGING = "paging";
            String DEVICESEAT = "deviceSeat";
            String DEVICENAME = "deviceName";
            String REVIEW = "review";
            String TYPE = "type";
            String CREATETIME = "createTime";
            String REMARKS = "remarks";
            String CLICK = "click";
            String STATUS = "status";
            String USERID = "userid";

        }
    }

    interface ReviewForm {
        interface DBCollums {
            String ID = "id";
            String FORMID = "formid";
            String SHEETLOGID = "sheetlogid";
            String SHEETID = "sheetid";
            String NUMBER = "number";
            String PAGING = "paging";
            String DEVICESEAT = "deviceSeat";
            String DEVICENAME = "deviceName";
            String REVIEW = "review";
            String TYPE = "type";
            String CREATETIME = "createTime";
            String REMARKS = "remarks";
            String STATUS = "status";

        }
    }

}
