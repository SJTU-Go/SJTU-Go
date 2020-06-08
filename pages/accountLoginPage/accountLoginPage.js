// pages/databaseGuide/databaseGuide.js

const app = getApp()
function json2Form(json) { 
  var str = []; 
  for(var p in json){ 
    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(json[p])); 
  } 
  return str.join("&"); 
} 
Page({
  data: { 
    adminName: '', 
    password:''
    },
    adminNameInput :function (e) { 
      this.setData({ 
        adminName:e.detail.value 
      }) 
      }, 
    passwordInput :function (e) { 
      this.setData({ 
        password:e.detail.value 
        }) 
      },
    onClickRegister:function()
    {wx.navigateTo({
      url: '../registrationPage/registrationPage',
    })


    },
    onClickSubmit:function(){
      if(this.data.adminName.length == 0 || this.data.password.length == 0){ 
        wx.showToast({ 
        title: '不能为空', 
        icon: 'loading', 
        duration: 2000 
        }) 
       }
       else { 
        var that=this
        console.log(that.data)
        var url="https://api.ltzhou.com/admin/adminlogin?name=".concat(that.data.adminName).concat("&pw=").concat(that.data.password)
        console.log(url)
        wx.request({
          url: 'https://api.ltzhou.com/admin/adminlogin',
          method:'POST',
          header: {
          'content-type': 'application/x-www-form-urlencoded'
          },
         data:json2Form({
           name:that.data.adminName,
           pw: that.data.password
         }),
      
          success (res){
            console.log(res)
            if(res.data.message=="login successfully!"){
              wx.setStorage({
                data:that.data.adminName,
                  
                key: 'admin',
              })
              wx.switchTab({
                url: '../index/index',
              })
              //登录校验设置
              wx.showToast({ 
              title: '登录成功', 
              icon: 'success', 
              duration: 2000 
              }) 
            }
            else{
              wx.showToast({ 
                title: '账号或密码错误', 
                icon: 'loading', 
                duration: 1000 
                }) 
            }
          
          }
           
        })
      }
      
      
        
        } 
        
       
  })