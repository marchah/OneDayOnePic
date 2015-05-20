<?php

function connectionBDD() {
        try
        {
            return new PDO('mysql:host=localhost;dbname=onedayonepic', 'root', 'root');
        }
        catch (Exception $e)
        {
            die('Erreur : ' . $e->getMessage());
        }
}