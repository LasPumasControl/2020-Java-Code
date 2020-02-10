// PixyCam2-9
// Baylee Carpenter
// FRC 2197
// This will gather target data from the Pixy cam
// and then send the x coordinate and distance to
// the rio over a PWM connection to the rio's analog input
//
// Need to get a regression formula to calculate distance
// This can be done using Pixymon and gathering Y 
// coordinates at varying distances

#include <SPI.h>  
#include <Pixy.h>

// This is the main Pixy object 
Pixy pixy;

void setup()
{
  pinMode(3,OUTPUT);
  pinMode(5,OUTPUT);
  
  Serial.begin(9600);
  Serial.print("Starting...\n");

  pixy.init();
}

void loop()
{ 
  static int i = 0;
  int j;
  uint16_t blocks;
  char buf[32]; 
  double x, y, w, h, d;
  double x2rio, d2rio;

  // need to sample y pixels to get an equation to 
  // calculate distance
  
  // grab blocks!
  blocks = pixy.getBlocks();
  
  // If there are detect blocks, print them!
  if (blocks)
  {
    i++;
    
    // do this (print) every 50 frames because printing every
    // frame would bog down the Arduino
    if (i%50==0)
    {
      sprintf(buf, "Detected %d:\n", blocks);
      Serial.print(buf);
      for (j=0; j<blocks; j++)
      {
        sprintf(buf, "  block %d: ", j);
        Serial.print(buf); 
        pixy.blocks[j].print();
      }

      x = pixy.blocks[1].x;
      y = pixy.blocks[1].y;
      w = pixy.blocks[1].width;
      h = pixy.blocks[1].height;

      // calculate distance here once equation is derived

      // changes x to 0-5
      x2rio = x * 0.01567398;
      analogWrite(3, x2rio);

      // need to do the same thing to d2rio
      
    }
  }  
}
