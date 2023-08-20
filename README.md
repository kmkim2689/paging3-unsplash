## Mock Unsplash Application using Paging3

### Reference
* https://youtu.be/2Tj0LO5L2Dk

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
      * prevPage & nextPage

---

### API Description

* Unsplash Developers
> https://unsplash.com/documentation

* Procedures
  1. Register Unsplash Developers
  2. Register an application -> Demo Mode : 50 requests per hour...
  3. Your Apps > Keys > get Access Key
    * Access Key = API Key
    * for API Request, this should be posted into "header"
  4. Unsplash API Guidelines
    * https://help.unsplash.com/en/articles/2511245-unsplash-api-guidelines
    * https://unsplash.com/documentation#public-authentication
    * Header name : Authorization
    * Header content : Client-ID YOUR_ACCESS_KEY

* API Request & Response Example
  * request : https://api.unsplash.com/photos?page=1&per_page=10&order_by=latest
  * response inspection
    * 10 different images
    * each image contains a lot of information
      * unique id, created_at, updated_at...
      * what we actually need...
        * id : id for image
        * urls => we can access to the actual image(hot links)
          * raw
          * full
          * regular
          * small
          * thumb
        * likes : num of likes
        * user : user info
          * id : identification string for user
          * username
          * portfolio_url : link
          * links > html(profile link)
          * etc...

---

### Project Preparation

1. dependencies

* refer build.gradle(both project and app)

2. properties

* Color.kt
* drawable

---

### Project Implementation

* Model : model > UnsplashImage.kt(Data Class)
  * Roles : Defining Data in Each Item Actually used in Whole Data + Database Table
    => annotate with @Entity and define the tableName 
  * to use kotlinx-serialization, annotate with @Serializable each data class & @SerialName each field if needed
  + Urls, User, UserLinks data class
    * @Embedded (Room) => Room Library가 nested된 클래스안의 필드들을 하나하나 인식하여 그 필드들이 하나의 데이터베이스의 필드의 일원으로 들어가도록 한다.
      * Marks a field of an Entity or POJO to allow nested fields (i.e. fields of the annotated field's class) to be referenced directly in the SQL queries.

* @SerializedName in Gson vs @SerialName in kotlinx
  * https://moon-i.tistory.com/entry/Gson-vs-kotlinx-serialization

          For example, if you have 2 classes:
      
          data class Coordinates (
              val latitude: Double,
              val longitude: Double
          )
          
          data class Address (
              val street: String,
              @Embedded
              val coordinates: Coordinates
          )
        
          Room will consider latitude and longitude as if they are fields of the Address class when mapping an SQLite row to Address.
          So if you have a query that returns street, latitude, longitude, Room will properly construct an Address class.
          If the Address class is annotated with Entity, its database table will have 3 columns: street, latitude and longitude.

* data
  * remote
    * UnsplashApi.kt(Interface)
      * for http calls using retrofit

* implementing dagger-hilt in project
  * MyApplication.kt => Application Class
    * annotate with @HiltAndroidApp
    * used by dagger-hilt library to generate "all the necessary codes" we need to actually inject all the dependencies in the project
  * MainActivity.kt
    * annotate with @AndroidEntryPoint
  * di
    * NetworkModule.kt

* implementing database
  * entity(table)
    * UnsplashImage.kt => for item
    * UnsplashRemoteKeys.kt => for remotemediator
      * store previous and next page
      * remote mediator can understand which page to request next

  * ***important : the main role of remote mediator***
    * handle the pagination from the server "automatically" by manipulating the query parameter dealing with "page"
    * whenever we request api,
      * by default the remote mediator can add the "page" query by itself by current page!!
      * ex) photos?page=1 => "page=1" will be automatically increase as you scroll down -> new data
  
  * dao(db queries)
    * data > local > UnsplashImageDao.kt for actual data of each item
      * **3 methods needed**
        * getting "all the items" from "local database"
          * return type : PagingSource<Key, Value>
          * Key : Page(Int), Value : Actual Item(UnsplashImage)
          * by using PagingSource class, we can paginate through room database => not getting all the items from local database at once, instead page by page
        * add items
        * delete all the items

    * data > local > UnsplashRemoteKeysDao.kt for Remote Mediator
      * **3 methods needed**
        * getRemoteKeys : getting a remote key with specific id
          * with one specific image id -> get single remote key for that specific image
        * addAllRemoteKeys
          * when item added in UnsplashImage table...
        * deleteAllRemoteKeys
          * remove all the records

  * database
    * data > local > UnsplashDatabase.kt
    * di > DatabaseModule.kt => for injecting

* implementing Remote Mediator for offline caching
  * purpose of Remote Mediator
    1. requesting new data from remote api
    2. caching the data from remote api into the local database

  * with remote mediator, we don't want to show the data from network directly in app
  * we want to show the data from 'local database'
    * local database -> single source of truth
    * that is... without network connection we can show the data

  * its action starts as the signal from the ui comes
    * whenever we need more data to load from the database
    * reach the last item of lazycolumn -> remote mediator "triggers" automatically -> request new page of data from the api -> the new page cached into the local database
  
  * to make a remote mediator class
    * data > paging > UnsplashRemoteMediator.kt
    * RemoteMediator 클래스의 load 함수는 이러한 데이터 로딩 및 동기화 과정을 수행하는 메서드입니다.
    * load 함수는 페이지를 로드하고 기존의 로컬 데이터와 원격 데이터 원본을 비교하여 새로운 데이터를 가져오거나 업데이트하는 역할을 합니다. 일반적으로 데이터 원본에서 페이지 단위로 데이터를 가져와 로컬 데이터베이스에 저장하거나 업데이트하게 됩니다.
    * load 함수는 PagingState와 LoadType 파라미터를 받습니다.
    * PagingState는 '현재' 페이징 '상태'를 나타내는 객체로, 페이지 수, 페이지 크기, 키 등의 정보를 포함합니다.
    * LoadType은 데이터 로딩 타입을 나타내는 열거형 값으로, 초기 로딩, 새로 고침, 이전 데이터 추가 로딩, 다음 데이터 추가 로딩 등을 구분할 수 있습니다.
    * load 함수는 이전에 로드된 데이터와 현재 상태를 기반으로 어떤 데이터를 가져와야 하는지 결정하고, 가져온 데이터를 로컬 데이터베이스에 삽입하거나 업데이트하여 페이징 데이터 소스에 새로운 데이터를 제공합니다. 이 과정은 사용자가 스크롤링하거나 데이터를 새로 고칠 때마다 호출됩니다.
    * 간단하게 말하면, RemoteMediator 클래스의 load 함수는 원격 데이터 소스에서 데이터를 가져와 로컬 데이터베이스에 저장하거나 업데이트하는 작업을 수행하며, 이로써 Paging3 라이브러리가 페이징된 데이터를 관리하고 유연한 방식으로 데이터를 로딩할 수 있도록 도와줍니다.
    
* Implementing a Repository
  * for ViewModel
  * 