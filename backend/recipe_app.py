from pydantic import BaseModel
from pymongo import MongoClient
from urllib.parse import quote_plus
import uuid
from typing import List
import json
from fastapi import FastAPI, File, UploadFile, HTTPException
import os
import base64
from groq import Groq
import time
api_keys = ['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', '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']

# MongoDB connection setup
def get_mongo_client():
    password = quote_plus("momimaad@123")  # Change this to your MongoDB password
    mongo_uri = f"mongodb+srv://hammad:{password}@cluster0.2a9yu.mongodb.net/"
    return MongoClient(mongo_uri)

db_client = get_mongo_client()
db = db_client["recipe"]
user_collection = db["user_info"]

# Pydantic models for user data
class User(BaseModel):
    first_name: str
    last_name: str
    email: str
    password: str

class UserData(BaseModel):
    email: str
    password: str

class UserToken(BaseModel):
    token: str

class RecipeData(BaseModel):
    name: str

class AltrecipeData(BaseModel):
    recipe_name: str
    dietary_restrictions: str
    allergies: List

class Ingredient(BaseModel):
    name: str
    quantity: str


class Recipe(BaseModel):
    recipe_name: str
    ingredients: List[Ingredient]
    directions: List[str]


class get_recipe_name(BaseModel):
    recipe_name: List[str]
    ingredients: List[List[str]]

# Data model for LLM to generate
class Alternative_Ingredient(BaseModel):
    name: str
    quantity: str

class Alternative_Recipe(BaseModel):
    recipe_name: str
    alternative_ingredients: List[Alternative_Ingredient]
    alternative_directions: List[str]

def encode_image(image_path):
    with open(image_path, "rb") as image_file:
        return base64.b64encode(image_file.read()).decode('utf-8')
class RecipeGenerationError(Exception):
    """Custom exception for recipe generation errors."""
    pass

def get_recipe(recipe_name: str) -> Recipe:

    groq_models = ["llama3-groq-70b-8192-tool-use-preview", "llama-3.1-70b-versatile", "llama3-70b-8192"]

    for api_key in api_keys:
        try:
             client = Groq(api_key=api_key)
             for groq_model in groq_models:
                 try:
                    chat_completion = client.chat.completions.create(
                        messages=[
                            {
                                "role": "system",
                                "content": f"""You are an expert agent to generate a recipes with proper and corrected ingredients and direction. Your directions should be concise and to the point and dont explain any irrelevant text.
                                You are a recipe database that outputs recipes in JSON.\n
                              The JSON object must use the schema: {json.dumps(Recipe.model_json_schema(), indent=2)}""",
                            },
                            {
                                "role": "user",
                                "content": f"Fetch a recipe for {recipe_name}",
                            },
                        ],
                        model=groq_model,
                        temperature=0,
                        # Streaming is not supported in JSON mode
                        stream=False,
                        # Enable JSON mode by setting the response format
                        response_format={"type": "json_object"},
                    )
                    return Recipe.model_validate_json(chat_completion.choices[0].message.content)

                 except Exception as e:
                     time.sleep(2)
                     continue


        except Exception as e:
            time.sleep(2)
            continue
    raise RecipeGenerationError("Server Error Try after some time.")


def Suggest_ingredient_alternatives(recipe_name: str, dietary_restrictions: str, allergies: List) -> Alternative_Recipe:
    groq_models = ["llama3-groq-70b-8192-tool-use-preview", "llama-3.1-70b-versatile", "llama3-70b-8192"]

    for api_key in api_keys:
        try:
            client = Groq(api_key=api_key)

            for groq_model in groq_models:
                try:
                    chat_completion = client.chat.completions.create(
                        messages=[
                            {
                                "role": "system",
                                "content": f"""
                                 You are an expert agent to suggest alternatives for specific allergies ingredients for the provided recipe {recipe_name}.
                
                                Please take the following into account:
                                - If the user has dietary restrictions, suggest substitutes that align with their needs (e.g., vegan, gluten-free, etc.) in alternative_directions and your alternative_directions should be concise and to the point.
                                -In ingredient you will recommend the safe ingredient for avoid any allergy and dietary restriction.
                                - Consider the following allergies {allergies} and recommend the safe ingredient to avoid this allergies.
                
                                recipe_name: {recipe_name}
                                Dietary Restrictions: {dietary_restrictions}
                                Allergies: {', '.join(allergies)}
                
                                You are a recipe database that outputs alternative recipes to avoid allergy and dietary_restrictions in JSON.\n
                                The JSON object must use the schema: {json.dumps(Alternative_Recipe.model_json_schema(), indent=2)}""",
                            },
                            {
                                "role": "user",
                                "content": f"""Fetch a alternative recipe for recipe_name: {recipe_name}
                                Dietary Restrictions: {dietary_restrictions}
                                Allergies: {', '.join(allergies)}""",
                            },
                        ],
                        model=groq_model,
                        temperature=0,
                        # Streaming is not supported in JSON mode
                        stream=False,
                        # Enable JSON mode by setting the response format
                        response_format={"type": "json_object"},
                    )
                    return Alternative_Recipe.model_validate_json(chat_completion.choices[0].message.content)
                except Exception as e:
                    time.sleep(2)
                    continue
        except Exception as e:
            time.sleep(2)
            continue

    raise RecipeGenerationError("Server Error Try after some time.")


def get_explanation(base64_image):

    api_keys = [
        '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',
        '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',
        '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',
        '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',
        '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'
    ]

    for api_key in api_keys:
        try:
            client = Groq(api_key=api_key)
            completion = client.chat.completions.create(
                model="llama-3.2-90b-vision-preview",
                messages=[
                    {
                        "role": "user",
                        "content": [
                            {
                                "type": "text",
                                "text": """Explain the following image in detail with the recipes names if it were present in images with expanation of ingredients. If there is one rcipe kindly mention it and if there are many then mention the total number of recipes and also mention their names and i want correct explanation."""
                            },
                            {
                                "type": "image_url",
                                "image_url": {
                                    "url": f"data:image/jpeg;base64,{base64_image}"
                                }
                            }
                        ]
                    }
                ],
                temperature=1,
                max_tokens=1024,
                top_p=1,
                stream=False,
                stop=None,
            )

            return completion.choices[0].message.content

        except Exception as e:
            time.sleep(2)
            continue
    raise RecipeGenerationError("Server Error Try after some time.")


def generate_recipe_info(context):
    groq_models = ["llama3-groq-70b-8192-tool-use-preview", "llama-3.1-70b-versatile", "llama3-70b-8192"]
    output_format = '''
    Kindly generate the following json output format and iwant this format same to same dont change anything and add irrelevant text 
    Generate a output according to json format:

    {'recipes': [it can be empty if there is no data about recipes in context and it conatin many recipes if there are multiple etc for example 
    {'recipe_name': '',
    'ingredients':[only three to four just or minimum]},
     {'recipe_name': '',
    'ingredients':[only three to four just or minimum]},
    {'recipe_name': '',
    'ingredients':[only three to four just or minimum]},
    so on 

    ]
    }
    '''

    for api_key in api_keys:
        try:
            client = Groq(api_key=api_key)
            for groq_model in groq_models:
                try:
                    chat_completion = client.chat.completions.create(
                        messages=[
                            {
                                "role": "user",
                                "content": f'''
                                Kindly analyze if there is one recipe then mention one recipe in json if multiple then according to it generate  a json.
                                 You are an expert agent to analyze the following context and find out recipes name with proper ingredients and i want the out[ut according to following json format and dont need to change anything.
                                 JSON OUTPUT FORMAT: {output_format}
                                 Consider the followng contexto answer to answer
                                 Context:
                             {context}
                                ''' + output_format
                            }
                        ],
                        temperature=0,
                        # Streaming is not supported in JSON mode
                        stream=False,
                        # Enable JSON mode by setting the response format
                        response_format={"type": "json_object"},
                        model=groq_model,
                    )

                    return chat_completion.choices[0].message.content
                except Exception as e:
                    time.sleep(2)
                    continue


        except Exception as e:
            time.sleep(2)
            continue
    raise RecipeGenerationError("Server Error Try after some time.")


app = FastAPI()

@app.post("/get_recipe/{token}")
async def get_recipe_response(token: str, recipe_user: RecipeData):
    try:
        user = user_collection.find_one({"token": token})
        if not user:
            raise HTTPException(status_code=401, detail="Invalid token")

        # Find user by email
        recipe_name = recipe_user.name
        response = get_recipe(recipe_name)
        return {
            "Response": response
        }
    except RecipeGenerationError as e:
        # Return a custom error response if the RecipeGenerationError is raised
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/get_recipe_alternative/{token}")
async def get_alternative_recipe_response(token: str, altrecipe_user: AltrecipeData):
    try:
        user = user_collection.find_one({"token": token})
        if not user:
            raise HTTPException(status_code=401, detail="Invalid token")

        response = Suggest_ingredient_alternatives(altrecipe_user.recipe_name, altrecipe_user.dietary_restrictions, altrecipe_user.allergies)
        return {
            "Response": response
        }
    except RecipeGenerationError as e:
        # Return a custom error response if the RecipeGenerationError is raised
        raise HTTPException(status_code=500, detail=str(e))



# Directory to save uploaded images
UPLOAD_DIR = "uploads"

# Ensure the upload directory exists
os.makedirs(UPLOAD_DIR, exist_ok=True)


# Endpoint to upload an image
@app.post("/upload-image/{token}")
async def upload_image(token: str, file: UploadFile = File(...)):
    try:

        user = user_collection.find_one({"token": token})
        if not user:
            raise HTTPException(status_code=401, detail="Invalid token")

        # Validate the file type
        if not file.filename.lower().endswith(('.png', '.jpg', '.jpeg')):
            raise HTTPException(status_code=400, detail="Invalid file type. Only PNG, JPG, and JPEG are allowed.")

        # Create a file path for saving the uploaded file
        file_path = os.path.join(UPLOAD_DIR, file.filename)

        # Save the file
        with open(file_path, "wb") as buffer:
            buffer.write(await file.read())

        base64_image = encode_image(file_path)
        context = get_explanation(base64_image)
        result = generate_recipe_info(context)
        json_result = json.loads(result)

        return {
            "Response": json_result
        }
    except RecipeGenerationError as e:
        # Return a custom error response if the RecipeGenerationError is raised
        raise HTTPException(status_code=500, detail=str(e))


# Endpoint to register a new user
@app.post("/register")
async def register_user(user: User):
    # Check if user already exists
    existing_user = user_collection.find_one({"email": user.email})
    if existing_user:
        raise HTTPException(status_code=400, detail="Email already registered")

    # Create user data
    user_data = {
        "first_name": user.first_name,
        "last_name": user.last_name,
        "email": user.email,
        "password": user.password,  # Store plaintext password (not recommended in production)
    }

    # Insert the user data into the user_info collection
    result = user_collection.insert_one(user_data)
    return {"msg": "User registered successfully", "user_id": str(result.inserted_id)}

# Endpoint to check user credentials and generate a token
@app.post("/get_token")
async def check_credentials(user: UserData):
    # Find user by email
    existing_user = user_collection.find_one({"email": user.email})

    # Check if user exists and password matches
    if not existing_user or existing_user["password"] != user.password:
        raise HTTPException(status_code=401, detail="Invalid email or password")

    # Generate a UUID token
    token = str(uuid.uuid4())

    # Update the user document with the token
    user_collection.update_one({"email": user.email}, {"$set": {"token": token}})

    return {
        "first_name": existing_user["first_name"],
        "last_name": existing_user["last_name"],
        "token": token,
    }

