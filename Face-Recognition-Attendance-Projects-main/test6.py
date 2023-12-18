import cv2
import numpy as np
import face_recognition
import os
import requests
import time

# URL của máy chủ Arduino (ESP32) để bật servo
arduino_on_url = "http://192.168.0.101/on"  # Thay thế bằng địa chỉ IP và cổng thích hợp

# URL của máy chủ Arduino (ESP32) để tắt servo
arduino_off_url = "http://192.168.0.101/off"  # Thay thế bằng địa chỉ IP và cổng thích hợp

# URL của máy chủ Arduino (ESP32) để bật còi
arduino_coi_on_url = "http://192.168.0.101/coiOn"  # Thay thế bằng địa chỉ IP và cổng thích hợp

# URL của máy chủ Arduino (ESP32) để tắt còi
arduino_coi_off_url = "http://192.168.0.101/coiOff"  # Thay thế bằng địa chỉ IP và cổng thích hợp

# Thư mục chứa hình ảnh để train model
training_images_path = 'Training_images'
images = []
classNames = []

# Lấy danh sách thư mục con trong thư mục Training_images
myList = os.listdir(training_images_path)

# Lặp qua các thư mục con và hình ảnh
for cl in myList:
    curPath = os.path.join(training_images_path, cl)

    # Lấy danh sách tất cả các tệp hình ảnh trong thư mục con hiện tại
    imagePaths = [os.path.join(curPath, img) for img in os.listdir(curPath)]

    for imgPath in imagePaths:
        curImg = cv2.imread(imgPath)
        images.append(curImg)
        classNames.append(cl)

# Hàm tìm mã hóa cho danh sách hình ảnh
def findEncodings(images):
    encodeList = []

    for img in images:
        img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        encode = face_recognition.face_encodings(img)[0]
        encodeList.append(encode)
    return encodeList

# Tìm mã hóa cho tất cả hình ảnh
encodeListKnown = findEncodings(images)
print('Encoding Complete')

# Khởi tạo camera
cap = cv2.VideoCapture(0)

# Biến để theo dõi xem có khuôn mặt được nhận dạng không
face_detected = False

# Biến để theo dõi trạng thái servo
servo_on = False

# Biến để theo dõi trạng thái còi
coi_on = False

# Biến để theo dõi trạng thái chế độ Auto
auto_mode = False

while True:
    success, img = cap.read()

    imgS = cv2.resize(img, (0, 0), None, 0.25, 0.25)
    imgS = cv2.cvtColor(imgS, cv2.COLOR_BGR2RGB)

    facesCurFrame = face_recognition.face_locations(imgS)
    encodesCurFrame = face_recognition.face_encodings(imgS, facesCurFrame)

    # Đặt trạng thái mặc định là không có khuôn mặt được nhận diện
    face_detected = False
    name = "Unknown"
    minFaceDis = 0
    check = True
    for encodeFace, faceLoc in zip(encodesCurFrame, facesCurFrame):
        matches = face_recognition.compare_faces(encodeListKnown, encodeFace)
        faceDis = face_recognition.face_distance(encodeListKnown, encodeFace)

        matchIndex = np.argmin(faceDis)
        minFaceDis = min(faceDis)

        if matches[matchIndex] and (1 - minFaceDis) >= 0.6:
            name = classNames[matchIndex].upper()
            # Đặt trạng thái có khuôn mặt được nhận diện
            face_detected = True

        y1, x2, y2, x1 = faceLoc
        y1, x2, y2, x1 = y1 * 4, x2 * 4, y2 * 4, x1 * 4
        cv2.rectangle(img, (x1, y1), (x2, y2), (0, 255, 0), 2)
        cv2.rectangle(img, (x1, y2 - 35), (x2, y2), (0, 255, 0), cv2.FILLED)
        cv2.putText(img, f'{name} {1-minFaceDis:.2f}', (x1 + 6, y2 - 6), cv2.FONT_HERSHEY_COMPLEX, 1, (255, 255, 255), 2)

    if not auto_mode:
        if not face_detected:
            if servo_on:
                # Nếu không có khuôn mặt và servo đang bật, gửi yêu cầu tắt servo (chế độ Open)
                response = requests.get(arduino_off_url)
                servo_on = False
                print("Servo đã tắt")
            elif name == "Unknown" and (1 - minFaceDis) >= 0.2 and minFaceDis != 0:
                # Nếu không có khuôn mặt và còi đang bật, gửi yêu cầu tắt còi
                response = requests.get(arduino_coi_on_url)
                coi_on = True
                print("Còi đã bật")  # Thêm thông báo khi còi tắt
        else:
            if not servo_on:
                # Nếu có khuôn mặt đã được nhận dạng và servo đang tắt, gửi yêu cầu bật servo
                response = requests.get(arduino_on_url)
                servo_on = True
                print("Servo đã bật")  # Thêm thông báo khi servo bật

    cv2.imshow('Webcam', img)

    key = cv2.waitKey(1) & 0xFF

    if key == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()
