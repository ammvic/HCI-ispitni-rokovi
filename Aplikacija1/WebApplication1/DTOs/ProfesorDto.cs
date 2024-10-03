using WebApplication1.DTOs;

public class ProfesorDto
{
    public int Id { get; set; }
    public string Username { get; set; }
    public string Ime { get; set; }
    public string Prezime { get; set; }
    public string Oblast { get; set; }
    public List<PredmetDto> Predmeti { get; set; }
}
