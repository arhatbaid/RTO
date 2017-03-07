package rto.example.com.rto.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import rto.example.com.rto.R;

public class AdapterMenu extends ArrayAdapter<String> {
    private Context context;
    private String[] objects;
    private int[] images = null;

    public AdapterMenu(Context context, int resource, String[] objects) {
        super(context, 0, objects);
        this.context = context;
        this.objects = objects;
        /*images = new int[]{R.drawable.home, R.drawable.my_accounts, R.drawable.trade_notification,
                R.drawable.change_service, R.drawable.invite_friends, R.drawable.settings, R.drawable.tnc,
                R.drawable.get_in_touch, R.drawable.help, R.drawable.logout};*/
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder v = null;
        if (convertView == null) {
            v = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell_menu_item, parent, false);
            v.textView = (TextView) convertView.findViewById(R.id.lblMenuName);
//            v.imageView = (ImageView) convertView.findViewById(R.id.imgMenu);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        v.textView.setText(objects[position]);
//        v.imageView.setBackgroundResource(images[position]);

        return convertView;
    }

    private class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
