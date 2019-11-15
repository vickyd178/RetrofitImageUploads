# RetrofitImageUploads
### Constants.php
```php
<?php
//Constants to connect with the database
define('DB_USERNAME', 'root');
define('DB_PASSWORD', '');
define('DB_HOST', 'localhost');
define('DB_NAME', 'images');

define('UPLOAD_PATH', '/images/');
```

### DbConnect.php
```php
<?php
 
//Class DbConnect
class DbConnect
{
    //Variable to store database link
    private $con;
    //Class constructor
    function __construct()
    {
    }
 
    //This method will connect to the database
    function connect()
    {
        //Including the constants.php file to get the database constants
        include_once dirname(__FILE__) . '/Constants.php';
 
        //connecting to mysql database
        $this->con = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);
 
        //Checking if any error occured while connecting
        if (mysqli_connect_errno()) {
            echo "Failed to connect to MySQL: " . mysqli_connect_error();
        }
 
        //finally returning the connection link 
        return $this->con;
    }
 
}
```

### Upload.php
```php
<?php

$db = new Upload;
$response = array();
class Upload{
    private $con;
    private $current_time;


    function __construct(){
        require_once dirname(__FILE__).'/DbConnect.php';
        $db = new DbConnect;
        $this->con = $db->connect();
        $current_time =  date('Y-m-d H:i:s');

        if($_FILES['image']['error'] === UPLOAD_ERR_OK && isset($_POST['name'])){

            $image = $_FILES['image']['tmp_name'];
            $name = $_POST['name'];
            $this->saveImage($name,$image,$this->getFileExtension($_FILES['image']['name']));
        }else{
            $response['error'] = true;
            $response['message'] = "Upload error try again.";
            echo json_encode($response);
        }
    }


    public function saveImage($name,$image,$imageExtension){

        $imageName = 'image_'.round(microtime(true) * 1000) .'.'. $imageExtension ;

        $imageUrl = dirname(__FILE__) . UPLOAD_PATH . $imageName;
                    
        if((move_uploaded_file($image, $imageUrl)))
        {
            $stmt = $this->con->prepare("INSERT INTO `image`(`name`, `image_url`) VALUES (?,?)");
            $stmt->bind_param("ss",$name,$imageName);
            if($stmt->execute()){
                $response['error'] = true;
                $response['message'] = "Upload successful.";
            }else{
                $response['error'] = true;
                $response['message'] = "Upload failed";
            }

        }else{
            $response['error'] = true;
            $response['message'] = "Upload error try again.";
        }

        echo json_encode($response);
    }

    function getFileExtension($file)
    {
        $path_parts = pathinfo($file);
        return $path_parts['extension'];
    }
}
```
### DATABASE
```mysql

CREATE TABLE `image` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `image_url` text NOT NULL,
  `ts` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `image`
  ADD PRIMARY KEY (`id`);
  
  ALTER TABLE `image`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;
```
### IMPORTANT
* Create images folder to save images in root directory where php files are saved

