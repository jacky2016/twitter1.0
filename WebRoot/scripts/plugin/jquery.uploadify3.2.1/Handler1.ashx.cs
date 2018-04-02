using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.IO;

namespace XK.JQuery.site.JS.uploadify
{
    /// <summary>
    /// Handler1 的摘要说明
    /// </summary>
    public class Handler1 : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";


            //接收上传后的文件
            HttpPostedFile file = context.Request.Files["Filedata"];
            //其他参数
            //string somekey = context.Request["someKey"];
            //string other = context.Request["someOtherKey"];
            //获取文件的保存路径
            string uploadPath =
                HttpContext.Current.Server.MapPath("UploadImages" + "\\");
            //判断上传的文件是否为空
            if (file != null)
            {
                if (!Directory.Exists(uploadPath))
                {
                    Directory.CreateDirectory(uploadPath);
                }
                //保存文件
                file.SaveAs(uploadPath + file.FileName);
                context.Response.Write("1");
            }
            else
            {
                context.Response.Write("0");
            }  
        }

        public bool IsReusable
        {
            get
            {
                return false;
            }
        }
    }
}