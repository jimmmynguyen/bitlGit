import cv2
import numpy as np
import face_recognition
import os
import paho.mqtt.client as mqtt
import threading

# Đường dẫn đến thư mục chứa hình ảnh của các người trong tập Training_images
path = 'Training_images'
images = []
classNames = []

myList = os.listdir(path)

for cl in myList:
    curPath = os.path.join(path, cl)
    imagePaths = [os.path.join(curPath, img) for img in os.listdir(curPath)]

    for imgPath in imagePaths:
        curImg = cv2.imread(imgPath)
        images.append(curImg)
        classNames.append(cl)

def findEncodings(images):
    encodeList = []

    for img in images:
        img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        encode = face_recognition.face_encodings(img)[0]
        encodeList.append(encode)
    return encodeList

encodeListKnown = findEncodings(images)
print('Encoding Complete')

cap = cv2.VideoCapture(0)

# Thông tin kết nối MQTT và ThingSpeak
mqtt_broker = "mqtt.thingspeak.com"
mqtt_port = 1883
username = "12"  # Thay thế bằng tên đăng nhập MQTT của bạn
channel_id = "2324544"  # Thay thế bằng ID kênh ThingSpeak của bạn
mqtt_topic = f"channels/{channel_id}/publish/fields/field1/{username}"
mqtt_api_key = "6AB55UPYU7QQX5Y7"  # Thay thế bằng MQTT API Key của ThingSpeak của bạn

client = mqtt.Client()
client.username_pw_set(username, password=mqtt_api_key)
client.connect(mqtt_broker, mqtt_port, 60)

def send_data_to_thingspeak(name):
    data = f"field1={name[0]}"
    client.publish(mqtt_topic, data)
    print("da gui")
while True:
    success, img = cap.read()

    imgS = cv2.resize(img, (0, 0), None, 0.25, 0.25)
    imgS = cv2.cvtColor(imgS, cv2.COLOR_BGR2RGB)

    facesCurFrame = face_recognition.face_locations(imgS)
    encodesCurFrame = face_recognition.face_encodings(imgS, facesCurFrame)

    for encodeFace, faceLoc in zip(encodesCurFrame, facesCurFrame):
        name = "Unknown"

        matches = face_recognition.compare_faces(encodeListKnown, encodeFace)
        faceDis = face_recognition.face_distance(encodeListKnown, encodeFace)

        matchIndex = np.argmin(faceDis)
        minFaceDis = min(faceDis)

        if matches[matchIndex] and (1 - minFaceDis) > 0.6:
            name = classNames[matchIndex].upper()
            # Sử dụng luồng để gửi dữ liệu bất đồng bộ
            data_sending_thread = threading.Thread(target=send_data_to_thingspeak, args=(name,))
            data_sending_thread.start()

        y1, x2, y2, x1 = faceLoc
        y1, x2, y2, x1 = y1 * 4, x2 * 4, y2 * 4, x1 * 4
        cv2.rectangle(img, (x1, y1), (x2, y2), (0, 255, 0), 2)
        cv2.rectangle(img, (x1, y2 - 35), (x2, y2), (0, 255, 0), cv2.FILLED)
        cv2.putText(img, name, (x1 + 6, y2 - 6), cv2.FONT_HERSHEY_COMPLEX, 1, (255, 255, 255), 2)

    cv2.imshow('Webcam', img)

    key = cv2.waitKey(1) & 0xFF

    if key == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
