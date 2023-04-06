import pickle
import sys
import json
import pandas as pd

def map_features(features):
    # convert the features string to a dataframe
    df = pd.DataFrame([features])
    
    # Read the JSON data into a Python object
    with open('dict.json', 'r') as f:
        model_dict = json.load(f)

    # use the map function to replace values in 'Horse' with corresponding values from the dictionary
    df['Sex'] = df['Sex'].replace(model_dict['Sex'])
    df['Horse'] = df['Horse'].replace(model_dict['Horse'])
    df['Colour'] = df['Colour'].replace(model_dict['Colour'])
    df['Venue'] = df['Venue'].replace(model_dict['Venue'])
    df['Jockey'] = df['Jockey'].replace(model_dict['Jockey'])
    df['Trainer'] = df['Trainer'].replace(model_dict['Trainer'])
    
    df["Trial"] = df["Trial"].astype(int)
    df["FirstUp"] = df["FirstUp"].astype(int)
    df["SecondUp"] = df["SecondUp"].astype(int)
    df["ThirdUp"] = df["ThirdUp"].astype(int)
    df["FourthUp"] = df["FourthUp"].astype(int)
    #boolToInt(df, "FourthUp")
 
    return df

# Read the input features from stdin
features = json.loads(sys.stdin.readline())
df = map_features(features)

# Load the trained model from the pickle file
with open("model1.pkl", "rb") as f:
    model = pickle.load(f)

# Make a prediction using the loaded model
try:
    # Write the prediction to stdout
    prediction = model.predict(df)
    print(prediction,flush=True)
except Exception as inst:
    print(inst.args, flush=True)     # arguments stored in .args
