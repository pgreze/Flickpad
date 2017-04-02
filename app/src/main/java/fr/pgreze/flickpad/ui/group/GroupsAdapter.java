package fr.pgreze.flickpad.ui.group;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.pgreze.flickpad.R;
import fr.pgreze.flickpad.domain.model.Group;
import fr.pgreze.flickpad.domain.model.Page;
import timber.log.Timber;

class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupViewHolder> {

    interface PageItemClickListener {
        void onGroupClick(int position, Group item);
    }

    private final LayoutInflater layoutInflater;
    private final Picasso picasso;
    private final List<Group> groups = new ArrayList<>(50);
    @Nullable
    private PageItemClickListener listener;
    private int currentPage;

    public GroupsAdapter(Context context, Picasso picasso) {
        this.layoutInflater = LayoutInflater.from(context);
        this.picasso = picasso;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupViewHolder(layoutInflater.inflate(R.layout.item_group, parent, false));
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        holder.bind(position, groups.get(position));
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public void addItems(Page<Group> page) {
        List<Group> newGroups = page.items();
        if (page.currentPage() > currentPage) {
            // Add items
            int begin = newGroups.size();
            groups.addAll(newGroups);
            // Update current page
            currentPage = page.currentPage();
            // Notify groups change for this set
            notifyItemRangeChanged(begin, newGroups.size());
        } else {
            // Obviously a refresh
            groups.clear();
            groups.addAll(page.items());
            notifyDataSetChanged();
        }
    }

    public void clear() {
        groups.clear();
        notifyDataSetChanged();
    }

    public GroupsAdapter setListener(@Nullable PageItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.group_item_icon)
        ImageView iconImg;
        @BindView(R.id.group_item_name)
        TextView nameTxt;

        int position;
        Group group;

        GroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onGroupClick(position, group);
            });
        }

        void bind(int position, Group group) {
            this.position = position;
            this.group = group;

            // Set texts
            nameTxt.setText(group.name());
            iconImg.setContentDescription(group.name());
            // Show group
            Timber.d("Display " + group);
            picasso.load(group.thumbnail())
                    .transform(new CircleTransform())
                    .placeholder(R.color.divider)
                    .error(R.color.accent)
                    .fit()
                    .centerCrop()
                    .into(iconImg);
        }
    }
}
