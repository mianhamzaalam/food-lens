# Project Overview: FoodLens

FoodLens is a cutting-edge AI-powered recipe application that leverages multiple advanced technologies to offer personalized and health-conscious recipe suggestions. This app utilizes MongoDB for storing and managing recipe data and employs Large Language Models (LLMs) to generate accurate ingredient quantities, directions, and recipe descriptions. Additionally, we use the Llama series models to detect alternative ingredients in case of allergies or specific dietary needs, ensuring a safe and tailored cooking experience for users.

The app also incorporates Vision Transformers (ViT) to analyze images of recipes and extract key information, such as the recipe name and ingredients. This enables the application to generate data-rich recipe suggestions directly from images, providing a comprehensive and intuitive user experience. Once the relevant data is extracted from images, the LLM is used to further refine the recipe information, ensuring it is accurate and properly formatted.

## Key Features:
- **MongoDB Integration**: Stores recipe data efficiently and ensures scalability.
- **LLMs for Recipe Generation**: Uses large language models to generate ingredients, quantities, and directions based on a given recipe.
- **Llama Models for Allergy and Disease Detection**: Detects alternative ingredients for individuals with allergies or specific dietary restrictions.
- **Vision Transformers for Image Analysis**: Detects and extracts recipe details from images, including the name and ingredients.
- **Data Extraction Using LLMs**: Analyzes and formats recipe details, ensuring high-quality recipe generation.

## How to Run the Backend

To set up and run the backend for FoodLens, follow these simple steps:

1. **Clone the GitHub Repository**: First, clone the repository containing the project:
   
   ```
   git clone https://github.com/mianhamzaalam/food-lens.git   ```

3. **Navigate to the Backend Directory**: Go to the backend directory where the server-side code is stored:
   
   ```
   cd backend```

3. **Install Required Dependencies**: Install all the necessary Python dependencies by using the following command:

   ```
   pip install -r requirements.txt```
   
4. **Run the Backend Server**: Start the backend server using Uvicorn:

   ```
   uvicorn recipe-app:app --reload```
   
This will start the FastAPI server, and the --reload option ensures that any changes you make to the code will automatically be reflected in the running application.
