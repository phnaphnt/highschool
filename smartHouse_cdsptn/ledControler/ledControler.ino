String sInput;

int ledPin[8] = {2, 3, 4, 5, 6, 7, 8, 9};
int buttonPin[8] = {A0, A1, A2, A3, A4, A5, A6, A7};

int button[8];
int ledStatus[8];
int phoneSignal[8];
int checkList[8] = {1, 1, 1, 1, 1, 1, 1, 1};

int ledNum = 0;
bool kt = false;

void setup() {
  for (int i = 0; i <= 7; i++) {
    pinMode(ledPin[i], OUTPUT);
    pinMode(buttonPin[i], INPUT_PULLUP);
  }
  Serial.begin(9600);
}

void loop() {
  checkIfButtonIsChanged();
  getSignal();
  controlLed();
  delay(50);
  if (Serial.available() > 0) {
    for (int i = 0; i <= 7; i++) {
      if (digitalRead(ledPin[i]) == LOW) {
        Serial.print('ON');
        Serial.println(i);
      }
      else {
        Serial.print('OFF');
        Serial.println(i);
      }
    }
  }
}

void checkIfButtonIsChanged() {
  for (int i = 0; i <= 7; i++) {
    button[i] = (digitalRead(buttonPin[i]) == LOW) ? 1 : 0;
    if (button[i] != checkList[i]) {
      ledStatus[i] = (ledStatus[i] == 1) ? 0 : 1;
      checkList[i] = button[i];
    }
  }
  delay(5);
}

void controlLed() {
  for (int i = 0; i <= 7; i++) {
    if (ledStatus[i] == 1) {
      digitalWrite(ledPin[i], HIGH);
    }
    else if (ledStatus[i] == 0) {
      digitalWrite(ledPin[i], LOW);
    }
  }
}

void getSignal() {
  int valueTime = 0;
  int timer = millis();
  int valueControl;
  while (Serial.available()) {
    sInput = String(Serial.read());
    kt = true;
  }
//-----------------------------------------------------------  
  if (!Serial.available()) {
    if (kt) {
      if (sInput.substring(0,5) == "timer"){
        valueTime = sInput.substring(5,sInput.length()).toInt();  
      }
      if (sInput.substring(0,2) == "on"){
        valueControl = sInput.substring(2,sInput.length()).toInt();  
      }
      if (sInput.substring(0,3) == "off"){
        valueControl = sInput.substring(3,sInput.length()).toInt();  
      }
//-----------------------------------------------------------      
      if (valueTime != 0){
        if ( (unsigned long) (millis() - timer) > valueTime) {
          if (digitalRead(ledPin) == LOW)
          {
            digitalWrite(ledPin, HIGH);
          } 
          else {
            digitalWrite(ledPin, LOW);
          }
          timer = millis();
        }
      }
//--------------------------------------------------------------    
      if (valueControl < 100 && valueControl > 9) {
        int a = valueControl / 10 - 1;
        int b = valueControl - (a + 1) * 10;
        phoneSignal[a] = b;
      }
      sInput = "";
      kt = false;
      for (int i = 0; i <= 7; i++) {
        ledStatus[i] = (phoneSignal[i] == 1) ? 1 : 0;
      }
    }
  }
}
