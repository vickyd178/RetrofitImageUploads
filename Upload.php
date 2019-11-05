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