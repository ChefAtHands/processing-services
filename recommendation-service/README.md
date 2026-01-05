# Recommendation Service

REST API service for providing recipe recommendations based on available ingredients.

## Endpoints

### GET /api/recommendations
Search recipes using user's saved ingredients.

**Query Parameters:**
- `userID` (required): User ID
- `offset` (optional, default: 0): Pagination offset
- `limit` (optional, default: 10): Number of results per page
- `minCarbs` (optional): Minimum carbohydrates
- `maxCarbs` (optional): Maximum carbohydrates
- `minProtein` (optional): Minimum protein
- `maxProtein` (optional): Maximum protein
- `minFat` (optional): Minimum fat
- `maxFat` (optional): Maximum fat
- `minCalories` (optional): Minimum calories
- `maxCalories` (optional): Maximum calories
- `category` (optional): Recipe category (breakfast, vegan, dessert, dinner, etc.)

**Example:**
```bash
GET /api/recommendations?userID=123&offset=0&limit=10&minProtein=50&category=dessert
```

**Response (200 OK):**
```json
{
  "recipes": [
    {
      "id": 2,
      "name": "Chocolate Cake",
      "description": "Rich chocolate dessert",
      "category": "dessert",
      "preparationTime": 60,
      "servings": 8,
      "calories": 550,
      "protein": 8,
      "carbs": 75,
      "fat": 25,
      "ingredients": [
        {
          "ingredientId": 5,
          "name": "milk",
          "quantity": 200,
          "unit": "ml"
        }
      ],
      "instructions": [
        "Mix dry ingredients",
        "Add wet ingredients",
        "Bake at 180C for 45 minutes"
      ]
    }
  ],
  "total": 1,
  "offset": 0,
  "limit": 10
}
```

### POST /api/recommendations
Search recipes using ingredients provided in request body.

**Query Parameters:** Same as GET endpoint

**Request Body:**
```json
{
  "ingredients": [
    {
      "name": "tomato",
      "quantity": 4,
      "ingredientID": 1
    },
    {
      "name": "meat",
      "quantity": 2,
      "ingredientID": 3
    },
    {
      "name": "milk",
      "quantity": 3,
      "ingredientID": 5
    }
  ]
}
```

**Example:**
```bash
POST /api/recommendations?userID=123&offset=0&limit=10&minProtein=50&category=dessert
Content-Type: application/json

{
  "ingredients": [
    {"name": "tomato", "quantity": 4, "ingredientID": 1}
  ]
}
```

**Response (200 OK):** Same format as GET endpoint

**Error Response (400 Bad Request):**
```json
{
  "status": 400,
  "error": "Bad Request",
  "validationErrors": {
    "ingredients[0].name": "Ingredient name is required"
  }
}
```

## Running the Service

```powershell
# From repository root
cd 'c:\Users\matej\Desktop\programiranje\VSC\šola\ChefAtHands'
mvn -pl processing-services/recommendation-service -am spring-boot:run
```

The service will start on port **8082** (configured in `application.properties`).

## Testing with curl

```powershell
# GET recommendations for user
curl -v "http://localhost:8082/api/recommendations?userID=123&offset=0&limit=10"

# GET with filters
curl -v "http://localhost:8082/api/recommendations?userID=123&minProtein=40&category=dinner"

# POST with custom ingredients
curl -v -H "Content-Type: application/json" -d '{\"ingredients\":[{\"name\":\"tomato\",\"quantity\":4,\"ingredientID\":1}]}' "http://localhost:8082/api/recommendations?userID=123"
```

## Architecture

- **Controller Layer** (`RecommendationController`): Handles HTTP requests/responses
- **Service Layer** (`RecommendationService`): Business logic for recipe matching and filtering (CDI/Spring bean)
- **DTOs**: Request/response models for API contracts
- **Models**: Domain entities (Recipe, RecipeIngredient)
- **Exception Handling**: Global exception handler for validation errors

