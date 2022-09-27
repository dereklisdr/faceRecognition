
from flask import Flask
from flask import request
import os
import requests

BACKEND_HOST = os.getenv('BACKEND_HOST')
SERVER_PORT = os.getenv('SERVER_PORT')

'''
BACKEND_HOST = "https://facebackend.azurewebsites.net"
SERVER_PORT = 80
'''

# Create Flask app
app = Flask(__name__, static_url_path='/', static_folder='static')

@app.route('/')
def index():
    return app.send_static_file("index.html")

@app.route('/image', methods=['POST'])
def image():
    response = requests.post(BACKEND_HOST, data=request.data)
    return response.content

@app.route('/greeting', methods=['GET'])
def greeting():
    response = requests.get(BACKEND_HOST + '/greeting')
    return response.content

@app.route('/validate', methods=['POST'])
def validate():
    response = requests.post(BACKEND_HOST + '/validate', data = request.data)
    return response.content

if __name__ == '__main__': 
    app.run(host="0.0.0.0", port = SERVER_PORT, debug=True)
