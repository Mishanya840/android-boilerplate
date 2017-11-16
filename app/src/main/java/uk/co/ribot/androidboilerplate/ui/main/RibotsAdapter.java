package uk.co.ribot.androidboilerplate.ui.main;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.macroplus.webplatform.dto.task.TaskDto;
import uk.co.ribot.androidboilerplate.R;

public class RibotsAdapter extends RecyclerView.Adapter<RibotsAdapter.RibotViewHolder> {

    private List<TaskDto> mRibots;

    @Inject
    public RibotsAdapter() {
        mRibots = new ArrayList<>();
    }

    public void setRibots(List<TaskDto> ribots) {
        mRibots = ribots;
    }

    @Override
    public RibotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ribot, parent, false);
        return new RibotViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RibotViewHolder holder, int position) {
        TaskDto ribot = mRibots.get(position);
//        holder.hexColorView.setBackgroundColor(Color.parseColor(ribot.profile().hexColor()));
        holder.nameTextView.setText(ribot.getTitle());
        holder.emailTextView.setText("none");
    }

    @Override
    public int getItemCount() {
        return mRibots.size();
    }

    class RibotViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_hex_color) View hexColorView;
        @BindView(R.id.text_name) TextView nameTextView;
        @BindView(R.id.text_email) TextView emailTextView;

        public RibotViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
