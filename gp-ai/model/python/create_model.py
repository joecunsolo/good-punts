# Import necessary libraries
import pandas as pd
import pickle
import json

from sklearn.model_selection import GridSearchCV
from sklearn.datasets import make_regression
from sklearn.model_selection import train_test_split
from sklearn.metrics import r2_score
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_squared_error
from matplotlib import pyplot as plt

def boolToInt(df, field):
    #convert a boolean field in a dataFram to an Int
    b_dict = {"true": 1, "false": 0}
    df[field] = df[field].map(b_dict)

def convert_df_to_float(df):
    #Trim the whitespace out first
    df = df.applymap(lambda x: x.strip() if isinstance(x, str) else x)

    # create a set from the 'Horse' column of data
    horse_set = set(df['Horse'])
    horse_dict = {s: i for i, s in enumerate(horse_set)}
    # create a set from the 'Colour' column of data
    colour_set = set(df['Colour'])
    colour_dict = {s: i for i, s in enumerate(colour_set)}
    # create a set from the 'Sex' column of data
    sex_set = set(df['Sex'])
    sex_dict = {s: i for i, s in enumerate(sex_set)}
    # create a set from the 'Venue' column of data
    venue_set = set(df['Venue'])
    venue_dict = {s: i for i, s in enumerate(venue_set)}
    # create a set from the 'Jockey' column of data
    jockey_set = set(df['Jockey'])
    jockey_dict = {s: i for i, s in enumerate(jockey_set)}
    # create a set from the 'Trainer' column of data
    trainer_set = set(df['Trainer'])
    trainer_dict = {s: i for i, s in enumerate(trainer_set)}
    
    # use the map function to replace values in 'Horse' with corresponding values from the dictionary
    df['Horse'] = df['Horse'].map(horse_dict)
    df['Colour'] = df['Colour'].map(colour_dict)
    df['Sex'] = df['Sex'].map(sex_dict)
    df['Venue'] = df['Venue'].map(venue_dict)
    df['Jockey'] = df['Jockey'].map(jockey_dict)
    df['Trainer'] = df['Trainer'].map(trainer_dict)
    
    df["Trial"] = df["Trial"].astype(int)
    df["FirstUp"] = df["FirstUp"].astype(int)
    df["SecondUp"] = df["SecondUp"].astype(int)
    df["ThirdUp"] = df["ThirdUp"].astype(int)
    boolToInt(df, "FourthUp")

    df = df.drop('Scratched', axis=1)
    df = df.drop('OddStart', axis=1)

    # Combine the JSON strings into a single JSON object
    b_dict = {"true": 1, "false": 0}
    json_obj = {
        'Horse': horse_dict,
        'Colour': colour_dict,
        'Sex': sex_dict,
        'Venue': venue_dict,
        'Jockey': jockey_dict,
        'Trainer': trainer_dict,
        'Trial': b_dict,
        'FirstUp': b_dict,
        'SecondUp': b_dict,
        'ThirdUp': b_dict,
        'FourthUp': b_dict
    }
    with open('../dict/dict.json', 'w') as f:
        json.dump(json_obj, f)
    
    return df


def tune_model(rf):
    # Define a range of values for the hyperparameters to test
    param_grid = {
        'n_estimators': [10, 50, 100, 500],
        'max_depth': [10, 20, 30, None],
        'min_samples_split': [2, 5, 10],
        'min_samples_leaf': [1, 2, 4]
    }

    # Create a grid search object and fit it to the data
    grid_search = GridSearchCV(estimator=rf, param_grid=param_grid, cv=5, scoring='neg_mean_squared_error')
    grid_search.fit(X_train, y_train)

    # Get the best hyperparameters and score
    best_params = grid_search.best_params_
    best_score = -grid_search.best_score_

    # Train a final model using the best hyperparameters
    final_rf = RandomForestRegressor(**best_params)
    
    return final_rf

def print_results(rf_preds, y_val):
    fig, ax = plt.subplots()
    # Map each onto a scatterplot we'll create with Matplotlib
    ax.scatter(x=rf_preds, y=y_val)
    ax.set(title="Some random data, created with JupyterLab!")
    plt.show()

def train_model(df):
    # Split the dataset into training and validation sets
    X_train, X_val, y_train, y_val = train_test_split(df.drop("Margin", axis=1), 
                                                  df["Margin"], 
                                                  test_size=0.02, 
                                                  random_state=42)

    # Initialize and fit the random forest model
    # Create a random forest regressor
    rf = RandomForestRegressor()
    #rf = tune_model(rf)
    rf.fit(X_train, y_train)

    # Make predictions on the validation set and calculate the mean squared error
    rf_preds = rf.predict(X_val)
    #rf_mse = mean_squared_error(y_val, rf_preds)

    # Print the mean squared errors for each model
    #print("Random Forest MSE:", rf_mse)

    print_results(rf_preds, y_val)
    return rf

#Import the data files
print("Starting import")
df = pd.read_csv('../data/history-220401.txt')
df = pd.concat([df, pd.read_csv('../data/history-220501.txt')], ignore_index=True)
df = pd.concat([df, pd.read_csv('../data/history-220601.txt')], ignore_index=True)
df = pd.concat([df, pd.read_csv('../data/history-220701.txt')], ignore_index=True)
df = pd.concat([df, pd.read_csv('../data/history-220901.txt')], ignore_index=True)
df = pd.concat([df, pd.read_csv('../data/history-221101.txt')], ignore_index=True)
df = pd.concat([df, pd.read_csv('../data/history-230101.txt')], ignore_index=True)
df = pd.concat([df, pd.read_csv('../data/history-230301.txt')], ignore_index=True)

df = convert_df_to_float(df)
df= df.dropna(axis= 0, how='any')
print(df.shape)

#Train the model
print("Training model")
rf = train_model(df)

# Save the model to disk
print("Saving model to disk")
filename = '../pickles/model1.pkl'
pickle.dump(rf, open(filename, 'wb'))
print("Saved!")