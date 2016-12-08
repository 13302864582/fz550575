package eq.tools.equlizer_globle.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import eq.tools.equlizer_globle.R;
import eq.tools.equlizer_globle.Util.Config;
import eq.tools.equlizer_globle.Util.SystemUtil;


/**
 * Created by Administrator on 2015/12/9.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static OnItemClickListener onItemClickListener ;
    private static OnItemLongClickListener onItemLongClickListener;
    private final Context mContext;

    public RecyclerAdapter(Context context){
        this.mContext = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView eqText;
        private final RelativeLayout item;
        private ImageView eq_select_img;

        public ViewHolder(View v){
            super(v);
            eqText = (TextView) v.findViewById(R.id.eqText);
            eq_select_img= (ImageView) v.findViewById(R.id.eq_select_img);
            item= (RelativeLayout) v.findViewById(R.id.itemLayout);
            item.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null)
                        onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
            v.findViewById(R.id.itemLayout).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(getAdapterPosition());
                    return true;
                }
            });
        }

        public TextView getEqText(){
            return eqText;
        }
    }

    public void setOnItemClickListener(OnItemClickListener l){
        onItemClickListener = l;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener l){
        onItemLongClickListener = l;
    }

    @Override
    public int getItemCount() {
        return SystemUtil.eqValList==null?0:SystemUtil.eqValList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.eqlist_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getEqText().setText(SystemUtil.eqValList.get(position).getName());
        holder.eq_select_img.setImageResource(position>SystemUtil.EQImages.length-1?R.drawable.eq_preselection_01_selector:SystemUtil.EQImages[position]);
        if(SystemUtil.mCurEqposition==position){
            holder.getEqText().setTextColor(Config.ACCENT_COLOR);
            holder.item.setBackgroundColor(Color.parseColor("#222222"));
            holder.eq_select_img.setSelected(true);
        }
        else{
            holder.getEqText().setTextColor(Color.WHITE);
            holder.item.setBackgroundColor(Color.parseColor("#2e2f31"));
            holder.eq_select_img.setSelected(false);
        }
    }

    public interface OnItemClickListener{
        public void onItemClick(int position);
    }

    public interface OnItemLongClickListener{
        public void onItemLongClick(int position);
    }

}
