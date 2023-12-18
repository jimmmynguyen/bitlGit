#include <WiFi.h>
#include <ESP32Servo.h>

const char* ssid = "OPPOA92";
const char* password = "12345670";
Servo servo;
int servoPin = 5;     // GPIO của ESP32 mà servo được nối tới
int servoAngle = 0;   // Góc mặc định cho servo
int coiPin = 26;       // GPIO khác để điều khiển còi
bool servoOn = false;  // Biến để theo dõi trạng thái của servo
bool coiOn = false;    // Biến để theo dõi trạng thái của còi

WiFiServer server(80);

void setup() {
  Serial.begin(115200);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Ket noi toi WiFi...");
  }

  Serial.println("Da ket noi toi WiFi");
  Serial.print("IP cua ESP32: ");
  Serial.println(WiFi.localIP());

  servo.attach(servoPin);
  pinMode(coiPin, OUTPUT);
  digitalWrite(coiPin, HIGH); // Đảm bảo rằng còi ở trạng thái tắt ban đầu

  server.begin();

  Serial.println("Truy cap trinh duyet va dieu khien servo hoac coi.");

  servo.write(servoAngle);
}

void loop() {
  WiFiClient client = server.available();
  if (client) {
    String request = client.readStringUntil('\r');
    if (request.indexOf("/on") != -1) {
      servoAngle = 45;  // Góc để bật servo (90 độ)
      servo.write(servoAngle);
      servoOn = true;
    }
    if (request.indexOf("/off") != -1) {
      servoAngle = 0;  // Góc để tắt servo (0 độ)
      servo.write(servoAngle);
      servoOn = false;
    }
    if (request.indexOf("/coiOn") != -1) {
      digitalWrite(coiPin, LOW);  // Bật còi
      coiOn = true;
      delay(1000); // Đợi 1 giây
      digitalWrite(coiPin, HIGH);  // Tắt còi sau 1 giây
      coiOn = false;
    }
  
    client.println("HTTP/1.1 200 OK");
    client.println("Content-Type: text/html");
    client.println();
    client.println("<html><body>");
    client.println("<h1>Dieu Khien Thiet Bi</h1>");
    client.println("<p><a href='/on'><button>Bat Servo</button></a></p>");
    client.println("<p><a href='/off'><button>Tat Servo</button></a></p>");
    client.println("<p><a href='/coiOn'><button>Bat Coi</button></a></p>");
    
    // Kiểm tra trạng thái của servo và hiển thị tương ứng
    if (servoOn) {
      client.println("<p>Servo: Da bat</p>");
    } else {
      client.println("<p>Servo: Da tat</p>");
    }

    client.println("</body></html>");
    client.stop();
  }
}
