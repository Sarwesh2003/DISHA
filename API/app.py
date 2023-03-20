from flask import Flask, request, jsonify, render_template
from flask_cors import CORS
from os.path import dirname, join
from ast import Store
from pyrebase import pyrebase


app = Flask(__name__)
CORS(app)

@app.route('/',methods=["GET","POST"])
def home():
    return "DISHA"

firebaseConfig = {
  'apiKey': "AIzaSyDlZzw8CXHIiaLXEbq9o6F3cr7JbGNYoKo",
  'authDomain': "disha-61129.firebaseapp.com",
  'databaseURL': "https://disha-61129-default-rtdb.firebaseio.com",
  'projectId': "disha-61129",
  'storageBucket': "disha-61129.appspot.com",
  'messagingSenderId': "489204231107",
  'appId': "1:489204231107:web:76ddb08dc672ec35902b76",
  'measurementId': "G-S6P3Y35CQJ",
  'serviceAccount': "key.json"
}
firebase=pyrebase.initialize_app(firebaseConfig)

db=firebase.database()
auth= firebase.auth()

@app.route('/with_url_variables/<string:aadhar>')
def with_url_variables(aadhar: str):
    
    result = db.child('ProfileData').equal_to(aadhar).get()
    
    return render_template('index.html', aadhar = str(result))
if __name__ == "__main__":
    # port = int(os.environ.get('PORT', 5000))
    app.run(debug=True, host = "0.0.0.0")  