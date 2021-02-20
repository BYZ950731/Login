package com.tonmx.inspection;

import java.util.List;

public class FormBean {


        private String result;
        private FormItem data;
        private int errorCode;
        private String errorMessage;

        public void setResult(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
        }

        public void setData(FormItem data) {
            this.data = data;
        }

        public FormItem getData() {
            return data;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

}
