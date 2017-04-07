package fr.pgreze.flickpad.data.flickr;

import fr.pgreze.flickpad.data.flickr.model.FlickrFullGroup;
import fr.pgreze.flickpad.data.flickr.model.FlickrFullPhoto;
import fr.pgreze.flickpad.data.flickr.model.FlickrFullUser;
import fr.pgreze.flickpad.data.flickr.model.FlickrGroups;
import fr.pgreze.flickpad.data.flickr.model.FlickrPhotos;
import fr.pgreze.flickpad.data.flickr.model.FlickrResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Flickr retrofit interface.
 */
public interface FlickrService {

    /** Flickr API base url */
    String BASE_URL = "https://www.flickr.com/services/rest/";

    /** Before any path */
    String BASE_PATH = "?format=json&nojsoncallback=1&method=";

    // Entity -> photos

    /** See https://www.flickr.com/services/api/flickr.groups.pools.getPhotos.html */
    @GET(BASE_PATH + "flickr.groups.pools.getPhotos")
    Observable<FlickrResponse<FlickrPhotos>> groupPhotos(
            @Query("group_id") String groupId);

    /** See https://www.flickr.com/services/api/flickr.people.getPhotos.html */
    @GET(BASE_PATH + "flickr.people.getPhotos")
    Observable<FlickrResponse<FlickrPhotos>> userPhotos(
            @Query("user_id") String userId);

    /** https://www.flickr.com/services/api/flickr.photos.search.html */
    @GET(BASE_PATH + "flickr.photos.search")
    Observable<FlickrResponse<FlickrPhotos>> tagPhotos(
            @Query("tags") String tags);

    // Entity info (deprecated if non necessary)

    /** See https://www.flickr.com/services/api/flickr.photos.getInfo.html */
    @Deprecated
    @GET(BASE_PATH + "flickr.photos.getInfo")
    Observable<FlickrResponse<FlickrFullPhoto>> photoInfo(@Query("photo_id") String photoId);

    /** See https://www.flickr.com/services/api/flickr.people.getInfo.html */
    @GET(BASE_PATH + "flickr.people.getInfo")
    Observable<FlickrResponse<FlickrFullUser>> userInfo(@Query("user_id") String userId);

    /** See https://www.flickr.com/services/api/flickr.groups.getInfo.html */
    @Deprecated
    @GET(BASE_PATH + "flickr.groups.getInfo")
    Observable<FlickrResponse<FlickrFullGroup>> groupInfo(@Query("group_id") String groupId);

    // Search

    /** See https://www.flickr.com/services/api/flickr.photos.search.html */
    @GET(BASE_PATH + "flickr.photos.search")
    Observable<FlickrResponse<FlickrPhotos>> searchPhotos(
            @Query("text") String text);

    /** See https://www.flickr.com/services/api/flickr.groups.search.html **/
    @GET(BASE_PATH + "flickr.groups.search")
    Observable<FlickrResponse<FlickrGroups>> searchGroups(
            @Query("text") String text);

    // Note: no people search available in public APIs
}
