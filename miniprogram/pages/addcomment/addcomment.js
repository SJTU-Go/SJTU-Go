// pages/walk/walk.js

Page({
  data: {
    appr:true,
    userID:0,
    subshow: false,
    commentInput:'',
    boxshow:true,
    datapass:'',
    // 搜索框状态
    inputShowed: true,
    //显示结果view的状态
    viewShowed: false,
    // 搜索框值
    inputVal: "",
    lista:Array(0),
    fakelist:[{distance:"100m",name:"上院西侧",content:"评论内容",username:"小王"}],
    subcomment:''
  },
  onLoad: function(options){
    var d = JSON.parse(options.RT)
  var that = this
  wx.getStorage({
    key: 'userID',
    success(res){that.data.userID =res.data}
  })
  wx.getStorage({
    key: 'commentlist',
    success:function(res){
      var apr = res.data[d].approveUsers
      for (var j in apr){console.log("aproving")
                  console.log(apr[j])
                if(that.data.userID==apr[j]){console.log("aprovingfalse")
                that.setData({appr:false})}
                }

      console.log(res.data[d])
      var ress=new Array(0)
      ress.push(res.data[d])
      console.log("logging")
      that.setData({lista:ress})
    
      wx.request({
        url: 'https://api.ltzhou.com/comments/subcomment?fatherID='+ress[0].commentID,
        method:"POST",
        success(res)
        {console.log(res.data)
          that.setData({subcomment:res.data})
      
        }
      })
    }
  })
  

  /*
  wx.request({
    url: 'https://api.ltzhou.com/comments/loc',
    data: {location:"POINT(121.437600 31.025940)"} ,
    method: 'POST',
    header: {
      'Content-type': 'application/x-www-form-urlencoded'
    },
    success: function (res) {
      console.log(res[0])
      wx.setStorage({
        data: res.data,
        key: 'test',
      })
      that.setData({
        list: res.data
      })
      console.log(res)
    }

  })*/;
}

,
viewcomment:function()
{console.log("tap")

},
commentInput :function (e) { 
  this.setData({ 
    commentInput:e.detail.value 
    }) 
  },
  updataInput :function () { 
    var addingcontent = {}
    console.log("updating")
    console.log(this.data.commentInput)
    addingcontent.contents = this.data.commentInput
    addingcontent.fatherID = this.data.lista[0].commentID
    addingcontent.relatedPlace = this.data.lista[0].relatedPlace

    wx.getStorage({
      key: 'userID',
      success(res){addingcontent.userID = res.data;
        wx.request({
          url: 'https://api.ltzhou.com/comments/addcomment',
          method:"POST",
          data:addingcontent,
          success(res)
          {console.log("adding")
          console.log(addingcontent)
            console.log(res.data)
          
        
          }
        })      

    

      wx.setStorage({
        data:addingcontent,
        key: 'addingcontent',
      })}
    })
    },
    viewsub:function(){
console.log("view")
this.setData({subshow:true})

    },
   removesub:function(){
      console.log("view")
      this.setData({subshow:false})
      
          },
  thumb:function(){
var comid = this.data.lista[0].commentID
    wx.getStorage({
      key: 'userID',
      success(res){
  
        var useri = res.data
        console.log('https://api.ltzhou.com/comments/like?commentID='+comid+'&userID='+useri)
        wx.request({
        url: 'https://api.ltzhou.com/comments/like?commentID='+comid+'&userID='+useri,
        method:'POST',
        success(res){
          console.log(res.data)
          this.setData({appr:false})}
      })}
    })



  }
})