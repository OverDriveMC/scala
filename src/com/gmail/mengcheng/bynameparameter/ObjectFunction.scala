package com.gmail.mengcheng.bynameparameter

object ObjectFunction extends App{
  def test(code: => Unit)={
    println("start");
    code //这行才会调用传入的代码块，写成code()亦可
    println("end");
  }
  /*
   *start
    when evaluated!
    bb
    end
   */
  //此处的代码块不会马上被调用
  test{
    println("when evaluated!");
    println("bb");
  }
  
  def test1(code : ()=> Unit){
    println("start")
    //若想调用传入的代码块，必须写成code()，否则不会调用
    code()
    println("end")
  }
  /*
   * when evaluated
      start
      bb
      end
   */
  test1{
    println("when evaluated")
    ()=>{
       println("bb") 
    }
  }
}