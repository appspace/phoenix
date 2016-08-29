#include <Arduino.h>
#include <math.h>
#include <ESP8266WiFi.h>
#include <WiFiClientSecure.h>
#include <ESP8266HTTPClient.h>
#include <constants.h>

#define DEBUG_ESP_SSL
#define DEBUG_ESP_PORT = Serial

ADC_MODE(ADC_VCC);

const char* ssid     = SSID_NAME; // Your ssid
const char* password = SSID_PASSWORD; // Your Password
const char* API_URL= API_GATEWAY_URL;
const char* API_KEY = API_GATEWAY_CLIENT_KEY;
const char* SSL_FINGERPRINT = API_GATEWAY_SSL_FINGERPRINT;
const int httpsPort = 443;

const int MAX_READINGS = 10;
int mySensVals[MAX_READINGS];

void clearArray();
void readAdc();
void postData();

void loop(void) {

}

void setup() {
  Serial.begin(115200);
  Serial.setDebugOutput(true);

  delay(10);
  clearArray();
  readAdc();
  delay(10);

  // Connect to WiFi network
  WiFi.mode(WIFI_STA);
  Serial.println();
  Serial.println();
  Serial.print("Chip Voltage: ");
  Serial.println(ESP.getVcc());
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");

  delay(1000);
  postData();
  delay(1000);
  ESP.deepSleep(5 * 60 * 1000 * 1000);
}

void readAdc() {
  for (int idx = 0; idx < MAX_READINGS; idx++) {
    //int vcc = analogRead(A0);
    int vcc = ESP.getVcc();
    mySensVals[idx]=vcc;
  }
  //
}

void clearArray() {
  for (int idx = 0; idx < MAX_READINGS; idx++) {
    mySensVals[idx]=0;
  }
}

void postData() {
  String data = "{";
  data = data + "\"chipId\":\""+ESP.getChipId()+"\",";
  data = data + "\"bootMode\":\""+ESP.getBootMode()+"\",";
  data = data + "\"bootVersion\":\""+ESP.getBootVersion()+"\",";
  data = data + "\"coreVersion\":\""+ESP.getCoreVersion()+"\",";
  data = data + "\"cpuFreq\":\""+ESP.getCpuFreqMHz()+"\",";
  data = data + "\"flashChipId\":\""+ESP.getFlashChipId()+"\",";
  data = data + "\"flashChipMode\":\""+ESP.getFlashChipMode()+"\",";
  data = data + "\"flashRealSize\":\""+ESP.getFlashChipRealSize()+"\",";
  data = data + "\"espSdkVersion\":\""+ESP.getSdkVersion()+"\",";
  data = data + "\"espResetReason\":\""+ESP.getResetReason()+"\",";
  data = data + "\"values\":[";
  for (int idx = 0; idx < MAX_READINGS; idx++) {
    if (idx!=0) {
      data = data + ",";
    }
    data = data + mySensVals[idx];
  }
  data = data + "]}";

  HTTPClient client;
  Serial.print("connecting to ");
  Serial.println(API_URL);

  client.begin(API_URL, SSL_FINGERPRINT);
  client.addHeader("x-api-key", API_KEY);

  int result = client.POST(data);
  Serial.println("status code: " + String(result));

  if(result > 0) {
    Serial.println("body:");
    Serial.println(client.getString());
  } else {
    Serial.print("FAILED. error:");
    Serial.println(client.errorToString(result).c_str());
    Serial.println("body:");
    Serial.println(client.getString());
  }

  client.end();
}
