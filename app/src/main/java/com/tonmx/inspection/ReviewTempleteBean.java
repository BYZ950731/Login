package com.tonmx.inspection;

import java.util.List;

public class ReviewTempleteBean {

        private String result;
        private List<ReviewTempleteItem> data;
        private int errorCode;
        private String errorMessage;
        public void setResult(String result) {
            this.result = result;
        }
        public String getResult() {
            return result;
        }

        public void setItem(List<ReviewTempleteItem> data) {
            this.data = data;
        }
        public List<ReviewTempleteItem> getItem() {
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
