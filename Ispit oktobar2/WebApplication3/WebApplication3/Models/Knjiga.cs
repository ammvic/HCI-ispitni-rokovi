namespace WebApplication3.Models
{
    public class Knjiga
    {
        public int Id { get; set; }
        public string Naslov { get; set; }
        public string Autor { get; set; }
        public string Zanr { get; set; }
        public string Opis { get; set; }
        public DateTime Datum_Izdavanja { get; set; }
    }


}
