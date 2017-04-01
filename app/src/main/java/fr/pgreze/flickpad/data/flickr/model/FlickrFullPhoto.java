package fr.pgreze.flickpad.data.flickr.model;

/**
 * Full object returned by Flickr API.
 * Root object: "photo"
 *
 * Photo URL:
 * https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg
 *
 * Example:
 * https://farm1.staticflickr.com/687/23350994122_739ce5691e_m.jpg
 *
 * Smalls: Use M for thumbnail
 * m 240 on longest side
 * n 320 on longest side
 *
 * Medium: Use Z for a list
 * z 640 on longest side
 * c 800 on longest side † > March 1st 2012
 *
 * Large: Use H for fullscreen if available, otherwise B
 * b 1024 on longest side * > May 25th 2010
 * h 1600 on longest side † > March 1st 2012
 */
@Deprecated
public abstract class FlickrFullPhoto {

    abstract String id();
    abstract String dateuploaded();

    /** Used for url resolution */
    abstract String secret();
    /** Used for url resolution */
    abstract int farm();
    /** Used for url resolution */
    abstract String server();

    /**

     "id": "23350994122",
     "secret": "739ce5691e",
     "server": "687",
     "farm": 1,
     "dateuploaded": "1449025614",
     "isfavorite": 0,
     "license": "0",
     "safety_level": "0",
     "rotation": 0,
     "owner": {
         "nsid": "54396106@N00",
         "username": "youtube.com/utahactor",
         "realname": "Ginger Kitties Four",
         "location": "Provo, Utah, USA",
         "iconserver": "1575",
         "iconfarm": 2,
         "path_alias": "zeusandphoebe"
     },
     "title": {
        "_content": "Zeus loves his Litter Robot"
     },
     "description": {
        "_content": " Click this link and order it from that page: <a href=\"http://mbsy.co/litter-robot/21728326\" rel=\"nofollow\">mbsy.co/litter-robot/21728326</a> - This link has the $25 discount embedded in it. If you use this link to purchase, you will receive the $25 discount at checkout under the Order Review screen. This discount link is valid until Litter Robot decides to no longer offer a discount.\ufeff"
     },
     "visibility": {
         "ispublic": 1,
         "isfriend": 0,
         "isfamily": 0
     },

     "dates": {
         "posted": "1449025614",
         "taken": "2015-12-01 20:05:40",
         "takengranularity": 0,
         "takenunknown": "1",
         "lastupdate": "1458563619"
     },
     "views": "501",
     "comments": {
        "_content": "7"
     },
     "tags": {
     "tag": [
         {
             "id": "5484932-23350994122-13448",
             "author": "54396106@N00",
             "authorname": "youtube.com/utahactor",
             "raw": "Zeus",
             "_content": "zeus",
             "machine_tag": 0
         },
         {
             "id": "5484932-23350994122-2231",
             "author": "54396106@N00",
             "authorname": "youtube.com/utahactor",
             "raw": "ginger",
             "_content": "ginger",
             "machine_tag": 0
         },
     ],
     "urls": {
         "url": [
             {
             "type": "photopage",
             "_content": "https://www.flickr.com/photos/zeusandphoebe/23350994122/"
             }
         ]
     },
     "media": "photo"
     */
}
