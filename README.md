# cbdb-ocr

##BackGround
该项目主要用于对古籍图片的识别，训练出一套自己的识别库。通过生成的识别库，对现有的资料进行识别。

##Tasks
项目中使用到的 OCR 技术是 tesseract-ocr[https://github.com/tesseract-ocr/tesseract]。第一阶段使用的是 3.05.00 版本。后面考虑更新成
4.0 版本。

##Finished
1. 图片处理工作，包括 png 转 tif, 切图等。
2. 图片对应的Box文件。
3. 生成基本识别库及合并。
4. 定时处理训练任务。
