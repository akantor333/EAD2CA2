using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System.Text.Json;
using Microsoft.Extensions.Configuration;
using AzureSamples.AzureSQL.Class;

namespace AzureSamples.AzureSQL.Controllers
{
    [Route("api")]
    [ApiController]
    public class PersonController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public PersonController(IConfiguration configuration)
        {
            _configuration = configuration;
        }
        [HttpGet("all")]
        public IEnumerable<Person> Get()
        {
            var persons = GetPersons();
            return persons;
        }
        private IEnumerable<Person> GetPersons()
        {
            var persons = new List<Person>();
            using (var connection = new SqlConnection(_configuration.GetConnectionString("CryptoDatabase")))
            {
                var sql = "SELECT PersonId, Username, BitcoinWalletAddress, EtheriumWalletAddress, CardanoWalletAddress, BinanceWalletAddress, LitecoinWalletAddress,btc.Amount as btcnum,eth.Amount as ethnum,ada.Amount as adanum,bnc.Amount as bncnum,ltc.Amount as ltcnum,btc.Cryptocurrency as btcname, eth.Cryptocurrency as ethname, ada.Cryptocurrency as adaname, bnc.Cryptocurrency as bncname, ltc.Cryptocurrency as ltcname FROM Person inner join Wallet as btc on Person.BitcoinWalletAddress = btc.WalletAddress left join Wallet as eth on Person.EtheriumWalletAddress = eth.WalletAddress left join Wallet as ada on Person.CardanoWalletAddress = ada.WalletAddress left join Wallet as bnc on Person.BinanceWalletAddress = bnc.WalletAddress left join Wallet as ltc on Person.LitecoinWalletAddress = ltc.WalletAddress";
                connection.Open();
                using SqlCommand command = new SqlCommand(sql, connection);
                using SqlDataReader reader = command.ExecuteReader();
                while (reader.Read())
                {
                    int PersonId = (int)reader["PersonId"];
                    string Username = reader["Username"].ToString();
                    Wallet btcwallet = new Wallet((int)reader["BitcoinWalletAddress"], reader["btcname"].ToString(), (int)reader["btcnum"]);
                    Wallet ethwallet = new Wallet((int)reader["EtheriumWalletAddress"], reader["ethname"].ToString(), (int)reader["ethnum"]);
                    Wallet adawallet = new Wallet((int)reader["CardanoWalletAddress"], reader["adaname"].ToString(), (int)reader["adanum"]);
                    Wallet bncwallet = new Wallet((int)reader["BinanceWalletAddress"], reader["bncname"].ToString(), (int)reader["bncnum"]);
                    Wallet ltcwallet = new Wallet((int)reader["LitecoinWalletAddress"], reader["ltcname"].ToString(), (int)reader["ltcnum"]);

                    var person = new Person(PersonId,Username, btcwallet,ethwallet,adawallet,bncwallet,ltcwallet);
                    persons.Add(person);
                }
            }
            return persons;
        }

        // Return one person
        [HttpGet("person/{id}", Name = "GetPersonById")]
        [ProducesResponseType(404)]
        [ProducesResponseType(200)]
        public ActionResult<Person> GetPersonById(int id)
        {
            bool found = false;
            var persons = new List<Person>();
            using (var connection = new SqlConnection(_configuration.GetConnectionString("CryptoDatabase")))
            {
                var sql = "SELECT PersonId, Username, BitcoinWalletAddress, EtheriumWalletAddress, CardanoWalletAddress, BinanceWalletAddress, LitecoinWalletAddress,btc.Amount as btcnum,eth.Amount as ethnum,ada.Amount as adanum,bnc.Amount as bncnum,ltc.Amount as ltcnum,btc.Cryptocurrency as btcname, eth.Cryptocurrency as ethname, ada.Cryptocurrency as adaname, bnc.Cryptocurrency as bncname, ltc.Cryptocurrency as ltcname FROM Person inner join Wallet as btc on Person.BitcoinWalletAddress = btc.WalletAddress left join Wallet as eth on Person.EtheriumWalletAddress = eth.WalletAddress left join Wallet as ada on Person.CardanoWalletAddress = ada.WalletAddress left join Wallet as bnc on Person.BinanceWalletAddress = bnc.WalletAddress left join Wallet as ltc on Person.LitecoinWalletAddress = ltc.WalletAddress where PersonId = "+id;
                connection.Open();
                using SqlCommand command = new SqlCommand(sql, connection);
                using SqlDataReader reader = command.ExecuteReader();
                while (reader.Read())
                {
                    int PersonId = (int)reader["PersonId"];
                    string Username = reader["Username"].ToString();
                    Wallet btcwallet = new Wallet((int)reader["BitcoinWalletAddress"], reader["btcname"].ToString(), (int)reader["btcnum"]);
                    Wallet ethwallet = new Wallet((int)reader["EtheriumWalletAddress"], reader["ethname"].ToString(), (int)reader["ethnum"]);
                    Wallet adawallet = new Wallet((int)reader["CardanoWalletAddress"], reader["adaname"].ToString(), (int)reader["adanum"]);
                    Wallet bncwallet = new Wallet((int)reader["BinanceWalletAddress"], reader["bncname"].ToString(), (int)reader["bncnum"]);
                    Wallet ltcwallet = new Wallet((int)reader["LitecoinWalletAddress"], reader["ltcname"].ToString(), (int)reader["ltcnum"]);

                    var person = new Person(PersonId, Username, btcwallet, ethwallet, adawallet, bncwallet, ltcwallet);
                    persons.Add(person);
                    found = true;
                }
            }
            if (found)
                return persons[0];
            else return null;
        }
    }
}
