
import os
from flask import Flask, request
import requests

# Create Flask app
app = Flask(__name__, static_url_path='/', static_folder='static')

@app.route('/')
def index():
    return app.send_static_file("index.html")

@app.route('/image', methods=['POST'])
def image():
    response = requests.post("https://facebackend.azurewebsites.net", data=request.data)
    return response.text

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=80, debug=True)

