//index.js
const app = getApp()

Page({
  data:
  {preference:['步行','共享单车','校园巴士'],
   method:['步行','校园巴士','共享单车'],
   banned:[],
   passnum:0
  },
  onLoad: function (options) {
    var that =this;
    wx.getStorage({
    key: 'preference',
    success: function(res){
    that.setData({
      preference:res.data,
    })
    if(that.data.preference.length==0){
    that.setData({
    status:false
    });
    }else{
    that.setData({
    status:true
    })
    }
    },
    fail: function(res) {
    console.log(res+'aaaaa')
    }
    });
    wx.getStorage({
      key: 'banned',
      success: function(res){
      that.setData({
        banned:res.data,
      })
      if(that.data.banned.length==0){
      that.setData({
      status:false
      });
      }else{
      that.setData({
      status:true
      })
      }
      },
      fail: function(res) {
      console.log(res+'aaaaa')
      }
      });
    },

  setPreference:function()
  {/*
    var pref=''
    var i = 0
    var pass ={"banlist":"","preferencelist":"","userID":1}
    var bann=''
    wx.getStorage({
    key: 'preference',
    success:function(res){
      
      var pre=res.data
    while(i<pre.length){
      pref = pref + pre[i]+","
      i = i + 1
    }
pass.preferencelist=pref
i = 0
wx.getStorage({
  key: 'banned',
  success:function(res){
    
   var pre=res.data
  while(i<pre.length){
    bann = bann+ pre[i]+","
    i = i + 1
  }

  pass.banlist = bann
  pass.userID = 106
  console.log(pass)
  console.log("passing")
    wx.request({
      url: 'https://api.ltzhou.com/user/preference/get',
      data:pass,
      method:"POST",
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
success(res){console.log(res.data)}
    })  

  
  }})
  }
  })
  */
    wx.navigateTo({
    url: '../preferencePage/preferencePage',})

  },

  removebanned:function(event)
  { var ban = this.data.banned;
    var id =event.currentTarget.dataset.id;
    var pre = this.data.preference;
    pre.push(ban[id]) 
    ban.splice(id,1);
    this.setData({banned:ban,preference:pre})
    wx.setStorage({ key:'preference',
    data:pre}),
    wx.setStorage({ key:'banned',
    data:ban})
//    wx.navigateTo({url: '../setPreferencePage/setPreferencePage',})

},
  up:function(event)
  {var id =event.currentTarget.dataset.id;
   var currpre=this.data.preference[id];
   var prelist = this.data.preference;
  if (id != 0 )
  {prelist[id]=prelist[id-1];
   prelist[id-1]=currpre
  }
this.setData({preference:prelist})
wx.setStorage({ key:'preference',
data:prelist})
  },
  down:function(event)
  {var id =event.currentTarget.dataset.id;
   var currpre=this.data.preference[id];
   var prelist = this.data.preference;
  if (id != prelist.length-1)
  {prelist[id]=prelist[id+1];
   prelist[id+1]=currpre
  }
this.setData({preference:prelist})
wx.setStorage({ key:'preference',
data:prelist})
  },



  addbanned:function(event)
  { var ban = this.data.banned;
    var id =event.currentTarget.dataset.id;
    var pre = this.data.preference;
    ban.push(pre[id]) 
    pre.splice(id,1);
    this.setData({banned:ban,preference:pre})
    wx.setStorage({ key:'preference',
    data:pre}),
    wx.setStorage({ key:'banned',
    data:ban})
//    wx.navigateTo({url: '../setPreferencePage/setPreferencePage',})
},




})
