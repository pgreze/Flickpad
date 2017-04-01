package fr.pgreze.flickpad.domain.flickr;

import java.util.Date;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import fr.pgreze.flickpad.data.flickr.FlickrService;
import fr.pgreze.flickpad.data.flickr.model.FlickrFullUser;
import fr.pgreze.flickpad.data.flickr.model.FlickrGroups;
import fr.pgreze.flickpad.data.flickr.model.FlickrPhotos;
import fr.pgreze.flickpad.data.flickr.model.FlickrResponse;
import fr.pgreze.flickpad.domain.model.Group;
import fr.pgreze.flickpad.domain.model.Page;
import fr.pgreze.flickpad.domain.model.Photo;
import fr.pgreze.flickpad.domain.model.User;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Flickr service interactor.
 */
@Singleton
public class FlickrInteractor {

    public static final String ENTITY_ICON_FORMAT =
            "http://farm{farm}.staticflickr.com/{server}/buddyicons/{id}.jpg";
    public static final String PHOTO_FORMAT =
            "https://farm{farm}.staticflickr.com/{server}/{id}_{secret}.jpg";
    public static final String DEFAULT_MEDIUM_SIZE = "M";
    public static final String DEFAULT_LARGE_SIZE = "H";

    private final FlickrService service;

    @Inject
    public FlickrInteractor(FlickrService service) {
        this.service = service;
    }

    public Observable<Page<Group>> groupPhotos(String groupId) {
        return service.groupPhotos(groupId)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(this::responseHandler)
                .flatMap(this::convertToGroups)
                .doOnError(e -> Timber.e(e, "Error while fetching groupPhotos"));
    }

    public Observable<Page<Photo>> userPhotos(String userId) {
        return service.userPhotos(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(this::responseHandler)
                .flatMap(this::convertToPhotos)
                .doOnError(e -> Timber.e(e, "Error while fetching userPhotos"));
    }

    public Observable<Page<Photo>> tagPhotos(String tags) {
        return service.tagPhotos(tags)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(this::responseHandler)
                .flatMap(this::convertToPhotos)
                .doOnError(e -> Timber.e(e, "Error while fetching tagPhotos"));
    }

    public Observable<User> userInfo(String userId) {
        return service.userInfo(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(this::responseHandler)
                .flatMap(this::convertToUser)
                .doOnError(e -> Timber.e(e, "Error while fetching userInfo"));
    }

    public Observable<Page<Photo>> searchPhotos(String text) {
        return service.searchPhotos(text)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(this::responseHandler)
                .flatMap(this::convertToPhotos)
                .doOnError(e -> Timber.e(e, "Error while fetching searchPhotos"));
    }

    public Observable<Page<Group>> searchGroups(String text) {
        return service.searchGroups(text)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(this::responseHandler)
                .flatMap(this::convertToGroups)
                .doOnError(e -> Timber.e(e, "Error while fetching searchGroups"));
    }

    // Error handling

    private <T> Observable<T> responseHandler(FlickrResponse<T> response) {
        if (response.response() != null) {
            //noinspection ConstantConditions
            return Observable.just(response.response());
        }
        return Observable.error(new FlickrError(response));
    }

    public static class FlickrError extends RuntimeException {

        private final FlickrResponse response;

        public FlickrError(FlickrResponse response) {
            this.response = response;
        }

        @Override
        public String toString() {
            return super.toString() + " (code=" + getCode() + ")";
        }

        public int getCode() {
            return response.code();
        }

        @Override
        public String getMessage() {
            return response.message();
        }
    }

    // Convert data to domain model

    private Observable<Page<Group>> convertToGroups(FlickrGroups page) {
        return Observable.fromIterable(page.group())
                .map(group -> Group.create(
                        group.nsid(), group.name(),
                        ENTITY_ICON_FORMAT
                                .replace("{farm}", String.valueOf(group.iconfarm()))
                                .replace("{server}", group.iconserver())
                                .replace("{id}", group.nsid()),
                        Integer.valueOf(group.members()), Integer.valueOf(group.topicCount())))
                .toList()
                .map(list -> Page.create(
                        page.page(), page.pages(),
                        page.perpage(), page.total(), list))
                .toObservable();
    }

    private Observable<Page<Photo>> convertToPhotos(FlickrPhotos page) {
        return Observable.fromIterable(page.photo())
                .map(photo -> Photo.create(
                        photo.id(), photo.title(), new Date(), photo.owner(), photo.ownername(),
                        PHOTO_FORMAT.replace("{secret}", photo.secret())
                                .replace("{farm}", String.valueOf(photo.farm()))
                                .replace("{server}", photo.server())
                                .replace("{size}", DEFAULT_MEDIUM_SIZE),
                        PHOTO_FORMAT.replace("{secret}", photo.secret())
                                .replace("{farm}", String.valueOf(photo.farm()))
                                .replace("{server}", photo.server())
                                .replace("{size}", DEFAULT_LARGE_SIZE)))
                .toList()
                .map(list -> Page.create(
                        page.page(), page.pages(),
                        page.perpage(), page.total(), list))
                .toObservable();
    }

    private Observable<User> convertToUser(FlickrFullUser user) {
        return Observable.just(User.create(
                user.nsid(),
                getName(user.username()),
                getName(user.realname()),
                ENTITY_ICON_FORMAT
                        .replace("{farm}", String.valueOf(user.iconfarm()))
                        .replace("{server}", user.iconserver())
                        .replace("{id}", user.nsid())));
    }

    private String getName(Map<String, Map<String, String>> name) {
        for (Map.Entry<String, Map<String, String>> entry: name.entrySet()) {
            for (Map.Entry<String, String> entry2: entry.getValue().entrySet()) {
                return entry2.getValue();
            }
        }
        return "";
    }
}
