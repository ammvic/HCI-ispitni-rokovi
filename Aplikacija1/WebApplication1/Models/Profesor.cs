namespace WebApplication1.Models
{
    public class Profesor
    {
        public int Id { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }
        public string Ime { get; set; }
        public string Prezime { get; set; }
        public string Oblast { get; set; }
        public ICollection<ProfesorPredmet> ProfesorPredmeti { get; set; }
    }
}