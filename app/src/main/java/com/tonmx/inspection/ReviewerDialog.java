package com.tonmx.inspection;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class  ReviewerDialog extends Dialog {


    public ReviewerDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }
    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }


        public ReviewerDialog.Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public ReviewerDialog.Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public ReviewerDialog.Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public ReviewerDialog.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public ReviewerDialog.Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public ReviewerDialog.Builder setPositiveButton(int positiveButtonText,
                                                          DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public ReviewerDialog.Builder setPositiveButton(String positiveButtonText,
                                                          DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public ReviewerDialog.Builder setNegativeButton(int negativeButtonText,
                                                          DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public ReviewerDialog.Builder setNegativeButton(String negativeButtonText,
                                                          DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public ReviewerDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final ReviewerDialog dialog = new ReviewerDialog(context,R.style.InspectionDialog);
            View layout = inflater.inflate(R.layout.reviewer_dialog_item, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            //(ListView) layout.findViewById(R.id.name_list)

            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.location_ok))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.location_ok))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.location_ok).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.location_cancel))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.location_cancel))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.location_cancel).setVisibility(
                        View.GONE);
            }
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            }
            WindowManager m = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
            WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
            attributes.height = (int) d.getHeight()*5/18;

            attributes.width = d.getWidth()/2;
            dialog.getWindow().setAttributes(attributes);
            dialog.setContentView(layout);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setDimAmount(0);
            return dialog;
        }

    }


}
