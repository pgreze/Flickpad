package fr.pgreze.flickpad.ui.home;

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
import fr.pgreze.flickpad.domain.model.Page;
import fr.pgreze.flickpad.domain.model.Photo;
import timber.log.Timber;

class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    interface PageItemClickListener {
        void onPhotoClick(int position, Photo item);
    }

    private final LayoutInflater layoutInflater;
    private final Picasso picasso;
    private final List<Photo> photos = new ArrayList<>(50);
    @Nullable
    private PageItemClickListener listener;
    private int currentPage;

    public PhotosAdapter(Context context, Picasso picasso) {
        this.layoutInflater = LayoutInflater.from(context);
        this.picasso = picasso;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(layoutInflater.inflate(R.layout.item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        holder.bind(position, photos.get(position));
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void addItems(Page<Photo> page) {
        List<Photo> newPhotos = page.items();
        if (page.currentPage() > currentPage) {
            // Add items
            int begin = newPhotos.size();
            photos.addAll(newPhotos);
            // Update current page
            currentPage = page.currentPage();
            // Notify photos change for this set
            notifyItemRangeChanged(begin, newPhotos.size());
        } else {
            // Obviously a refresh
            photos.clear();
            photos.addAll(page.items());
            notifyDataSetChanged();
        }
    }

    public void clear() {
        photos.clear();
        notifyDataSetChanged();
    }

    public PhotosAdapter setListener(@Nullable PageItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo_item_title_txt)
        TextView titleText;
        @BindView(R.id.photo_item_img)
        ImageView imgView;

        int position;
        Photo photo;

        PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imgView.setOnClickListener(v -> {
                if (listener != null) listener.onPhotoClick(position, photo);
            });
        }

        void bind(int position, Photo photo) {
            this.position = position;
            this.photo = photo;

            // Set texts
            titleText.setText(photo.title());
            imgView.setContentDescription(photo.title());
            // Show photo
            // TODO: loading and error drawables
            Timber.d("Display " + photo);
            picasso.load(photo.mediumUrl())
                    .placeholder(R.color.divider)
                    .error(R.color.accent)
                    .fit()
                    .centerCrop()
                    .into(imgView);
        }
    }
}
