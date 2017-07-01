# Posit
An implementation of Posit numbers for Java

A Posit number represents a real number in mathematics. The accuracy and dynamic range of the number is selectable by the user.
Typically a number of bits is selected to fit on a target computer system: 16, 32, 64, etc.

Posit numbers are described by John L. Gustafson and Isaac Yonemoto in the paper "Beating Floating Point at its Own Game: Posit Arithmetic"
   - http://www.johngustafson.net/pdfs/BeatingFloatingPoint.pdf
   
"Posits beat floats at their own game: guessing their way through a calculation while incurring
rounding errors. Posits have higher accuracy, larger dynamic range, and better closure. They
can be used to produce better answers with the same number of bits as floats, or (what may
be  even  more compelling)  an  equally  good  answer with fewer bits.
Since  current systems  are bandwidth-limited, using smaller operands means higher speed and less power use.
Because they work like floats, not intervals, they can be regarded as a drop-in replacement
for floats"   
<p>
Read more to find out about the Posit binary representation:
![Posit format](https://github.com/beckerdo/Posit/blob/master/positformat.png)
