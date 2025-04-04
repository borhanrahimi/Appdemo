package edu.utsa.cs3443.picpick;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import edu.utsa.cs3443.picpick.model.Photo;

public class ImageAdapter extends BaseAdapter {

    private final Context context;
    private final List<Photo> photos;

    public ImageAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return photos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image;

        if (convertView == null) {
            image = new ImageView(context);
            image.setLayoutParams(new ViewGroup.LayoutParams(250, 250));
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setPadding(4, 4, 4, 4);
        } else {
            image = (ImageView) convertView;
        }

        try {
            InputStream inputStream = context.getAssets().open(photos.get(position).getFilePath());
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            image.setImageBitmap(bitmap);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }
}
