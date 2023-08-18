## Mock Unsplash Application using Paging3

### Intro

* Techs/Things Dealt with This Project
  - Paginating Data from Network -> Paginating Data from Room(Local Database) Library
  - The Way to Use "Remote Mediator" with "Remote Keys" for Caching Data
    - After caching the data into the local, we can access to the same data in the android application without network connection

* Application Description
  * Two Screens
    * Home Screen
      * Display 'all' the images from Unsplash API
      * Photo Items inside a LazyColumn
    * Search Screen
      * get request for searching images
    
* Database Description
  * takes "two" database tables for paginating
    1. the table for storing data of ui items(for here, data related to image)
      * id, likes, url, name...
    2. the table for remote keys
      * the table used by "remote mediator"
      * cf) remote mediator : the class of paging3 library that allows us to automatically cache the data
        * contains "next and previous" page number info -> remote mediator can notice which page to request next from the network(api)
  
  * paginate the data from the api response page by page
    * as user reaches to the bottom of the page, new data will be loaded

---

### Paging3 Library

* What is Paging3 Library?
  * To Paginate Data from Two Different Data Sources
    * Local Database(Room)
    * Network(API)

* Paginating the Data?
  ![image](https://github.com/zipdabang/android/assets/101035437/0abfcf6a-aad3-4831-bdf4-00813c433dcd)
  * "Providing" the Data to Application "Page by Page"
  * When getting the data from database, we don't have to read all the data from the database all at once
    => read them page by page!!!
  * the data will be loaded in small chunks...
  * as the user scrolls the list, the next chunk will be loaded

* Core Features of Paging3
  * In-memory caching for the paged data
  * good harmony with Kotlin Coroutines and Flow(First-class support) as well as RxJava
  * Automatically requests the correct page when the user has scrolled to the end of the list
  * Ensures that multiple requests aren't triggered at the same time
  * Offline Caching
  * Tracks the loading state
  * Map/Filter operator can be used

* Classes of Paging3 / Other Libraries used for Paginating 

  1. Paging Source
    * retrieves the data from single data source
      * network / local database
    * parameters
      * key : usually the page number... to determine which page to load
        * page number with integer value 
      * value : actual item data - the type of the data that will be loaded and displayed to the users

  2. Room Library
    ![image](https://github.com/zipdabang/android/assets/101035437/03590c28-6683-4cda-91ec-cd240f637c5a)
    * supports paging3 library by default
      => don't have to implement your own paging source class
    * add the PagingSource with Wrapper Class, as the return type of the dao function

  3. Paging Data
    * A container for "Paged" data from a "single generation" of loads
    * Scroll the list -> Bottom of the page(End of the page) -> Paging Data & Paging Source are notified -> new page data received if there is one
    * each refresh of data will have a separate corresponding page data
  
  4. Paging Config
    * To "control how and when" to query Paging Data from Paging Source
    * Configure behavior within a pager
    * Parameters
      * Page Size - # of items loaded at once from the Paging Source
        * warning : the page size should be much more than the number of items that can be shown in a single screen scope
        * small size of item - up to 100 / big size of item(facebook...) - 10~20
      * Initial Load Size - define a requested load size for initial
        * typically larger than pageSize 
        * whenever we load some data in application initially -> the number actually be higher a little bit
        * initial(default) : pageSize * 3
      * Max Size - for efficiently using memory
        * define the maximum number of items that may be loaded into the paging data before pages should be dropped
        * used to cap the number of items in memory by dropping pages
        * in the case user scrolls back

  5. Remote Mediator => Facilitates Offline Caching(load page data network -> database)
    ![image](https://github.com/zipdabang/android/assets/101035437/9236e9df-e594-4bb7-8eca-91a3172198bb)
    * to combine a local storage with a remote storage(queries)
    * to provide a consistent data flow to the user, whether the network is connected or not
    * not to load the data directly into the ui
    * data : remote network - (Remote Mediator) -> local database<Single Source Of Truth> --> ui
    * only the data that has only been cached into the local database will be loaded into the app
  
  6. Remote Keys
    * remote mediator uses to tell the backend service which data to load next
    * related to the remote keys entity