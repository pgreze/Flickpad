package fr.pgreze.flickpad.data.flickr.model;

/**
 * Full group representation.
 * Root object: "group"
 *
 * Useful values not included in light object:
 * - description
 */
@Deprecated
public abstract class FlickrFullGroup {

    /**

     Group icon:
     http://farm${iconfarm}.staticflickr.com/${iconserver}/buddyicons/${id}_m.jpg
     Used by website:
     http://c${iconfarm}.staticflickr.com/${iconfarm}/${iconserver}/buddyicons/${id}_m.jpg?1375106756

     Example:
     http://farm4.staticflickr.com/3686/buddyicons/51035612836@N01_s.jpg

     s 10x10
     m 60x60
     ALWAYS USE m!!

     "group": {

     "id": "51035612836@N01",
     "path_alias": "api",
     "iconserver": "3686",
     "iconfarm": 4,
     "name": {
        "_content": "Flickr API"
     },
     "description": {
        "_content": "A Flickr group for Flickr API projects. \n\nDriving awareness of the Flickr API, projects that use it and those incredible ideas that programmatically exposed systems produce. Think Google API + Amazon API + Flickr API with a bit of GMail thrown in.\n\nThe developers of Flickr rightly pointed out they want to keep technical discussions directly related to the API on the mailing list.\n\nIf you have photos or screenshots that illustrate your use of the API, then feel free to add them. Please be selective though - people don't necessarily want to see a dozen almost identical screenshots of your app - just one or two that show off the most interesting aspects. \n\nPlease don't add non-API related photos here. Not even Kittens, Babies or Sunsets. They will be rejected."
     },
     "rules": {
        "_content": "This group is dedicated to asking question about the Flickr API, and providing feedback to users of the API.\n\nAny photos not related to the Flickr API or an app developed using the Flickr API will be removed."
     },
     "members": {
        "_content": "17675"
     },
     "pool_count": {
        "_content": "783"
     },
     "topic_count": {
        "_content": "4166"
     },
     "privacy": {
        "_content": "3"
     },
     "lang": null,
     "ispoolmoderated": 1,
     "roles": {
         "member": "Member",
         "moderator": "Moderator",
         "admin": "Admin"
     },
     "blast": {
         "_content": "The old-style Flickr Authentication API has been deprecated since July 31st 2012. Only the new OAuth authentication is now officially supported. More info <a href=\"http://code.flickr.com/blog/2012/01/13/farewell-flickrauth/\" rel=\"nofollow\">here</a>. \n\nThe Flickr API now only supports <a href=\"https://www.flickr.com/groups/api/discuss/72157644984640397/\">HTTPS</a> since June 2014. Attempts to call the old http endpoints will result in an error being returned.",
         "date_blast_added": "1406814036",
         "user_id": "78188"
     },
     "throttle": {
         "count": "1",
         "mode": "day"
     },
     "restrictions": {
         "photos_ok": 1,
         "videos_ok": 1,
         "images_ok": 1,
         "screens_ok": 1,
         "art_ok": 1,
         "safe_ok": 1,
         "moderate_ok": 0,
         "restricted_ok": 0,
         "has_geo": 0
     }
     },

     */
}
