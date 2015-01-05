global.PATH_API = __dirname;

var express = require('express');
var app = express();
var port = process.env.PORT || 5060;

var mysql      = require('mysql');

var configDB = require('./config/database.js');
var constantes = require('./config/constantes.js');

var pool = mysql.createPool({
  host     : configDB.host,
  user     : configDB.user,
  password : configDB.password,
  database : configDB.database
});

pool.query(configDB.database);

app.get('/init', function(req, res) {
    pool.getConnection(function(err, connection) {
	if (err) {
	    console.log(err);
	    res.sendstatus(500);
	    return ;
	}
	var newUser  = {nbuser: 1};
	connection.query('INSERT INTO time_synchro SET ?', newUser, function(err, results) {
	    connection.release();
	    if (err) {
		console.log(err);
		res.sendStatus(500);
		return ;
	    }
	    res.send({userId: results.insertId, timeSynchro: results.insertId});
	});
    });
});

app.get('/categories', function(req, res) {
    pool.getConnection(function(err, connection) {
	if (err) {
	    console.log(err);
	    res.sendstatus(500);
	    return ;
	}
	connection.query('SELECT * FROM categorie ORDER BY id', function(err, rows, fields) {
	    connection.release();
	    if (err) {
		console.log(err);
		res.sendStatus(500);
		return ;
	    }
	    res.send(rows);
	});
    });
});

app.get('/picture/:idCategorie(\\d+)/:idUser(\\d+)', function(req, res) {
//    req.params.categorie
//req.params.idUser


    pool.getConnection(function(err, connection) {
	if (err) {
	    console.log(err);
	    res.sendstatus(500);
	    return ;
	}
	var query = '';
	if (req.params.idCategorie > 0)
	    query = 'WHERE idCategorie=' + connection.escape(req.params.idCategorie);
	connection.query('SELECT * FROM save_last_pic WHERE iduser= '+ connection.escape(req.params.idUser), function(err, row) {
	    if (err)
		console.log(err);
	    if (row.length == 0)
		console.log(constantes.Error.UnknowUserId + req.params.idUser);
	    else if (row.length > 1)
		console.log(constantes.Error.ManyEntryForTheSameUserId + req.params.idUser);
	    else
		query = (query.length == 0 ? 'WHERE ': query + ' AND ') + 'id != ' + row[0].idpicture;
	    var saveLastPic = row;
	    connection.query('SELECT * FROM picture ' + query + ' ORDER BY RAND() LIMIT 1', function(err, row) {
		if (err) {
		    console.log(err);
		    res.sendStatus(500);
		    return ;
		}
		res.sendFile(global.PATH_API + row[0].path, function (err) {
		    if (err) {
			console.log(err);
			res.sendStatus(500);
			return ;
		    }
		});
		var query2 = '';
		if (saveLastPic.length > 0)
		    query2 = 'UPDATE save_last_pic SET idUSer=' + connection.escape(req.params.idUser) + ', idpicture=' + row[0].id + ' WHERE id=' + saveLastPic[0].id;
		else
		    query2 = 'INSERT INTO save_last_pic SET idUSer=' + connection.escape(req.params.idUser) + ', idpicture=' + row[0].id;
		connection.query(query2, function(err) {
		    connection.release();
		    if (err) {
			console.log(err);
			return ;
		    }
		});
	    });
	});
    });
});

app.listen(port);
console.log('The magic happens on port ' + port);
