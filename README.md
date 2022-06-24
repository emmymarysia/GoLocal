Original App Design Project - README Template
===

# GoLocal

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
This app aims to connect people with local businessDataModels and experiences. With the map feature, users can search for unique places to try while on a trip, or small businessDataModels to shop at home, and filter based on interests/type of businessDataModel. With guideDataModels, people can explore other users' recommendations. Users can also create their own guideDataModels and add friends on the app using their profile page.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** social/travel
- **Mobile:** Having the app on your phone makes finding new places on the go easier, using location and maps functionality. Push notifications can remind you about places you were interested in.
- **Story:** Allows users to discover local spots and small businessDataModels, and plan adventures with friends.
- **Market:** People that are traveling to a new place and want to explore that city, as well as people who care about shopping locally rather than buying from big chains can use this app.
- **Habit:** Push notifications reminding users of what they’ve searched for (like Amazon) can build habit by bringing people back to the app. Friend features can also build habits if people can see what their friends are interested in.
- **Scope:** The scope of the app will begin with the essential features - being able to look at and filter a map to find local spots, being able to explore guideDataModels to a location (such as best coffee shops in the area), and user profiles people can customize with their interests.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

- Register new user
- Login
- Explore/search map
- List of user-created guideDataModels for specific interests/locations
- Profile page users can customize with their interests, and that will display guideDataModels they’ve created
- Page to create a guideDataModel (title it, add locations and descriptions)
- View the attributes of a specific businessDataModel
- Complete view of a specific guideDataModel when clicked

**Optional Nice-to-have Stories**

- Add overlay to map to display icons specific to type of location (food, hike, clothes, etc.)
- Be able to filter/search the map based on a specific product you’re looking to buy and attributes of a businessDataModel (eco friendly, etc.)
- Create plans for a trip/day out that you can add friends on the app to
- Users can post photos of places they visit using their phone camera
- Create an events calendar, and add events (like farmers markets or concerts) to the map

### 2. Screen Archetypes 

- **Login/Register**
  - Register new user
  - Login
- **Map view**
  - Explore/search map
  - Overlay to display icons
  - Filter the map based on specific products
- **Stream**
  - List of user-created guideDataModels for specific interests/locations
- **Profile**
  - Profile page
- **Creation**
  - Page to create a guideDataModel
  - Create plans for a trip with friends
  - Users can post photos of places they visit using their phone camera
- **Detail**
  - View the attributes of a specific businessDataModel
  - Complete view of a specific guideDataModel when clicked
- **Calendar**
  - Events calendar

### 3. Navigation

**Tab Navigation** (Tab to Screen)

- Map View
- Guides list
- User profile
- (optional) Events Calendar

**Flow Navigation** (Screen to Screen)
*italics indicates optional feature page*

- **Map View**
  - Detail view of a specific businessDataModel
  - *Add a businessDataModel to a trip plan*
  - *Post photos when you visit*
  - Filter search
- **Guides list**
  - Create a guideDataModel
  - Complete view of a specific guideDataModel when clicked
- **User profile**
  - Create a guideDataModel
  - Search for/add friends
  - *Create plans for a trip with friends*
  - *Post photos of a businessDataModel you visited*
- ***Events Calendar***
  - *Detail view for an event (same as for a businessDataModel)*
  - *Add an event to a trip plan*

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="https://user-images.githubusercontent.com/26172675/173665445-1a5af2f3-2978-435b-b2fd-4fc5f4dcd9bf.png" width=600>!



### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
User
| Property | Type | Description |
|----------|------|-------------|
| username | String | the user's username |
| friends | Array of Users | an array of the other users the given user has added as friends |
| bio | String | the bio/description a user has written for their profile |
| publishedGuides | Array of Guides | an array of the guideDataModels the user has published |
| likedGuides | Array of Guides | an array of the guideDataModels a user has liked/saved |

Guide
| Property | Type | Description |
|----------|------|-------------|
| title | String | the title of the guideDataModel |
| author | Pointer to a user | the user who has created the guideDataModel |
| likedBy | Array of users | a list of the users who have liked this guideDataModel |
| businessDataModelList | Array of businessDataModels | a list of the businessDataModels a user has added to this guideDataModel |

Business
| Property | Type | Description |
|----------|------|-------------|
| name | String | the name of the businessDataModel |
| address | String | the address of the businessDataModel |
| description | String | the description of the businessDataModel a user has chosen to write for a guideDataModel |
| image | File | the image a user has chosen to attach to this businessDataModel |


### Networking
- **Map view**
  - autocomplete request from Foursquare as a user types in a search
  - request from Foursquare to return list of businessDataModels matching a search/filters
  - request from Google Maps to display a map and display pins to businessDataModels returned by Foursquare request
- 
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]

- place search (places/search)
- get place details (places/{fsq_id})
- get place photos (places/{fsq_id}/photos)
- autocomplete (autocomplete)

