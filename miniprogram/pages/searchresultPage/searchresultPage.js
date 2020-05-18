//index.js
const app = getApp()
Page({
  data:{
    currentData : 0,
    walk:{},bus:{},
    index:['walk','bus'],
    value : new Array(),
    method:["步行","校园巴士"],
    preference:["步行","校园巴士"],
    preferencelist: new Array()
  },
  onLoad:function(options){
    // 页面初始化 options为页面跳转所带来的参数
    var that =this;
    var tem;
    var valuetem=new Array();
    var pre = new Array();
    var i;
    var j = 0;
    var preres = new Array();
wx.request({
 url: 'https://api.ltzhou.com/navigate/bus',
 method:'POST',
 header: {
   'content-type': 'application/json'
 },
 data:{
  "arrivePlace": "POINT (121.435505 31.026303)",
  "beginPlace": "图书馆",
  "departTime": "2020/05/11 12:05:12",
  "passPlaces": [
    "学生服务中心"
  ]
},

 success (res) {
   tem = res.data
   console.log(tem)
   that.setData({bus:tem})
   valuetem.push(tem)

   wx.request({
    url: 'https://api.ltzhou.com/navigate/walk',
    method:'POST',
    header: {
      'content-type': 'application/json'
    },
    data:{
      "arrivePlace": "POINT (121.435505 31.026303)",
      "beginPlace": "图书馆",
      "departTime": "2020/05/11 12:05:12",
      "passPlaces": []
    },
   
    success (res) {
      tem = res.data
      console.log(tem)
      that.setData({walk:tem})
      valuetem.push(tem)
      that.setData({value:valuetem})
      console.log(that.data.value)
      console.log("1")
   
   for(j=0;j<that.data.preference.length;j++){
   for (i=0;i<that.data.value.length;i++){
   if(that.data.value[i].type==that.data.preference[j]){
   pre.push(i)}}}
   for (i=0;i<pre.length;i++){
   preres.push(that.data.value[pre[i]])
   
   }
   
   
   
   
   that.setData({preferencelist:preres})
   console.log(that.data.preferencelist)
   console.log("2")
   
    }}     )
   



  }}     )









  },
  checkCurrent:function(e){
    const that = this;
 
    if (that.data.currentData === e.target.dataset.current){
        return false;
    }else{
 
      that.setData({
        currentData: e.target.dataset.current
      })
    }},
  onReady:function(){
    // 页面渲染完成
  },
  onShow:function(){
    // 页面显示
  },
  onHide:function(){
    // 页面隐藏
  },
  onUnload:function(){
    // 页面关闭
  }
})

