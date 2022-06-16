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
This app aims to connect people with local businesses and experiences. With the map feature, users can search for unique places to try while on a trip, or small businesses to shop at home, and filter based on interests/type of business. With guides, people can explore other users' recommendations. Users can also create their own guides and add friends on the app using their profile page.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** social/travel
- **Mobile:** Having the app on your phone makes finding new places on the go easier, using location and maps functionality. Push notifications can remind you about places you were interested in.
- **Story:** Allows users to discover local spots and small businesses, and plan adventures with friends.
- **Market:** People that are traveling to a new place and want to explore that city, as well as people who care about shopping locally rather than buying from big chains can use this app.
- **Habit:** Push notifications reminding users of what they’ve searched for (like Amazon) can build habit by bringing people back to the app. Friend features can also build habits if people can see what their friends are interested in.
- **Scope:** The scope of the app will begin with the essential features - being able to look at and filter a map to find local spots, being able to explore guides to a location (such as best coffee shops in the area), and user profiles people can customize with their interests.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

- Register new user
- Login
- Explore/search map
- List of user-created guides for specific interests/locations
- Profile page users can customize with their interests, and that will display guides they’ve created
- Page to create a guide (title it, add locations and descriptions)
- View the attributes of a specific business
- Complete view of a specific guide when clicked

**Optional Nice-to-have Stories**

- Add overlay to map to display icons specific to type of location (food, hike, clothes, etc.)
- Be able to filter/search the map based on a specific product you’re looking to buy and attributes of a business (eco friendly, etc.)
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
  - List of user-created guides for specific interests/locations
- **Profile**
  - Profile page
- **Creation**
  - Page to create a guide
  - Create plans for a trip with friends
  - Users can post photos of places they visit using their phone camera
- **Detail**
  - View the attributes of a specific business
  - Complete view of a specific guide when clicked
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
  - Detail view of a specific business
  - *Add a business to a trip plan*
  - *Post photos when you visit*
  - Filter search
- **Guides list**
  - Create a guide
  - Complete view of a specific guide when clicked
- **User profile**
  - Create a guide
  - Search for/add friends
  - *Create plans for a trip with friends*
  - *Post photos of a business you visited*
- ***Events Calendar***
  - *Detail view for an event (same as for a business)*
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
| publishedGuides | Array of Guides | an array of the guides the user has published |
| likedGuides | Array of Guides | an array of the guides a user has liked/saved |

Guide
| Property | Type | Description |
|----------|------|-------------|
| title | String | the title of the guide |
| author | Pointer to a user | the user who has created the guide |
| likedBy | Array of users | a list of the users who have liked this guide |
| businessList | Array of businesses | a list of the businesses a user has added to this guide |

Business
| Property | Type | Description |
|----------|------|-------------|
| name | String | the name of the business |
| address | String | the address of the business |
| description | String | the description of the business a user has chosen to write for a guide |
| image | File | the image a user has chosen to attach to this business |


### Networking
- **Map view**
  - autocomplete request from Foursquare as a user types in a search
  - request from Foursquare to return list of businesses matching a search/filters
  - request from Google Maps to display a map and display pins to businesses returned by Foursquare request
- 
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]

- place search (places/search)
- get place details (places/{fsq_id})
- get place photos (places/{fsq_id}/photos)
- autocomplete (autocomplete)

