package cn.hnist.pany.criminalintent;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;

public class PictureDialogFragment extends DialogFragment {

    public static PictureDialogFragment newInstance(String path){
        Bundle args = new Bundle();
        args.putString("path",path);

        PictureDialogFragment pictureDialogFragment = new PictureDialogFragment();
        pictureDialogFragment.setArguments(args);
        return pictureDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //获取到之前传过来的路径
        String path = getArguments().getString("path");
        //创建一个dialog
        final Dialog dialog = new Dialog(getActivity());
        //设置dialog的布局,为之前创建的布局文件,里面仅有一个ImageView
        dialog.setContentView(R.layout.picture_crime);
        //找到控件
        ImageView imageView = dialog.findViewById(R.id.iv);
        //使用 PictureUtils 类的工具来获得缩放的 Bitmap
        imageView.setImageBitmap(PictureUtils.getScaleBitmap(path,getActivity()));
        //设置点击事件，当点击图片时候，dialog消失。
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }
}