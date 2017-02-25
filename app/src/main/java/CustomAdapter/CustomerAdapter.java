package CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sd.ibook.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Model.InfoModelClass;

/**
 * Created by Sajib Chandra Das on 7/1/2015.
 */
public class CustomerAdapter extends ArrayAdapter<InfoModelClass> {

    private Context mContext;
    private List<InfoModelClass> mFriendsInformation;
    private ArrayList<InfoModelClass> mFriendsInfo;

    public CustomerAdapter(Context context, List<InfoModelClass> friendsInformation) {
        super(context, R.layout.row_customer_list, friendsInformation);
        context=mContext;
        mFriendsInformation=friendsInformation;
        ;
        this.mFriendsInfo = new ArrayList<InfoModelClass>();
        this.mFriendsInfo.addAll(mFriendsInformation);

    }

    @Override
    public int getCount() {
        return mFriendsInformation.size();
    }

    @Override
    public InfoModelClass getItem(int position) {

        if (position<mFriendsInformation.size()){
            return mFriendsInformation.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder{

        private TextView Name;
        private ImageView Thumbnail;
    }

    public void Update(){
        mFriendsInformation.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=convertView;
        Holder mHolder;

        InfoModelClass friendsInformation=getItem(position);
        if (view==null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.row_customer_list,null);
            mHolder=new Holder();
            mHolder.Name=(TextView)view.findViewById(R.id.tvName);
            mHolder.Thumbnail=(ImageView)view.findViewById(R.id.iv_Image_thumbnail);

            view.setTag(mHolder);
        }else{
            mHolder=(Holder)view.getTag();
        }

        if (mHolder.Name!=null){
            mHolder.Name.setText(friendsInformation.getName());
        }

        if (mHolder.Thumbnail!=null){
            mHolder.Thumbnail.setImageDrawable(friendsInformation.getThumbnail(getContext()));
        }

        return view;
    }
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mFriendsInformation.clear();
        if (charText.length() == 0) {
            mFriendsInformation.addAll(mFriendsInfo);
        } else {
            for (InfoModelClass im : mFriendsInfo) {
                if (im.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    mFriendsInformation.add(im);
                }
            }
        }
        notifyDataSetChanged();
    }
}
