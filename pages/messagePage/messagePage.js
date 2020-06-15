// pages/messagePage/messagePage.js
const app = getApp()
function json2Form(json) { 
  var str = []; 
  for(var p in json){ 
    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(json[p])); 
  } 
  return str.join("&"); 
} 
Page({

  /**
   * 页面的初始数据
   */
  data: {
    isChecked1: false,
    message:{},
    adminID:1,
    admin:'',
mailName:''
  },
  searchInput :function (e) { 
    this.setData({ 
      mailName:e.detail.value 
    }) 
    },
    search :function (e) { 
      console.log(e) 
      },
filter:function(e){ 
  var that=this
        console.log(e) 
        if (!e.detail.value){
          console.log(1+11111111)
    wx.request({
      url: 'https://api.ltzhou.com/feedback/inbox',
      method:'POST',
      header: {
      'content-type': 'application/x-www-form-urlencoded'
      },
     data:json2Form({
       adminID:that.data.adminID
     }),
  
      success (res){
        console.log(res)
        that.setData({message:res.data})
        
      }
       
    })
        }
        else{
          console.log(1+1)
          wx.request({
            url: 'https://api.ltzhou.com/feedback/inbox',
            method:'POST',
            header: {
            'content-type': 'application/x-www-form-urlencoded'
            },
           data:json2Form({
             adminID:that.data.adminID
           }),
        
            success (res){
              console.log(res)
              var mm= new Object();
              var count=0;

              for (var i= res.data.length-1;i>=0;i--){
                if  (!res.data[i].adminID){

                  mm[count]=res.data[i]
                  for (var j=count;j>0;--j){
                    console.log(j,yes,mm[j].time,mm[j-1].time,mm[j].time>mm[j-1].time)
                    if (mm[j].time>mm[j-1].time){
                      console.log(yes,mm[j].time,mm[j-1].time,mm[j].time>mm[j-1].time)
                      var temp=mm[j-1]
                      mm[j-1]=mm[j]
                      mm[j]=temp

                    }
                    else{break}
                  }
                  count+=1
                }
                console.log(mm)
              }
              console.log(mm)
              that.setData({message :mm})
              
            }
             
          })
        }
        },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var that=this
    wx.getStorage({
      key: 'admin',
      success:function(res){
        console.log(res.data)
        that.setData({admin:res.data})
        
      }
    })
    wx.getStorage({
      key: 'adminID',
      success:function(res){
        console.log(res.data)
        that.setData({adminID:res.data})
        
      }
    })
    console.log(that.data)
    
    wx.request({
      url: 'https://api.ltzhou.com/feedback/inbox',
      method:'POST',
      header: {
      'content-type': 'application/x-www-form-urlencoded'
      },
     data:json2Form({
       adminID:that.data.adminID
     }),
  
      success (res){
        console.log(res)
              var mm= new Object();
              var count=0;

              for (var i= res.data.length-1;i>=0;i--){
                if  (res.data[i].time){

                  mm[count]=res.data[i]
                  for (var j=count;j>0;--j){
                    //console.log(j,1,mm[j].time,mm[j-1].time,mm[j].time>mm[j-1].time)
                    if (mm[j].time>mm[j-1].time){
                      //console.log(1,mm[j].time,mm[j-1].time,mm[j].time>mm[j-1].time)
                      var temp=mm[j-1]
                      mm[j-1]=mm[j]
                      mm[j]=temp

                    }
                    else{break}
                  }
                  count+=1
                }
                //console.log(mm)
              }
              console.log(mm)
              that.setData({message :mm})
        
      }
       
    })
  
  


  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  detail: function (e) {
    var that = this
    console.log(e)
    var index=e.currentTarget.dataset.index
    wx.setStorage({
      data: this.data.message[index].feedbackID,
        
      key: 'cuFB',
    })
    wx.navigateTo({url: '../fbDetail/fbDetail', })
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})